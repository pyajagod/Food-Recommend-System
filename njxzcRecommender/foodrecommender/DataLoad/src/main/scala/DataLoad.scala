import java.net.InetAddress

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import utils.PropertiesUtil

import scala.util.matching.Regex

object DataLoad {
    private val FOODS_DATA_PATH: String = PropertiesUtil.getProperty("settings.properties", "FOODS_DATA_PATH")
    private val RATINGS_DATA_PATH: String = PropertiesUtil.getProperty("settings.properties", "RATINGS_DATA_PATH")
    private val TAGS_DATA_PATH: String = PropertiesUtil.getProperty("settings.properties", "TAGS_DATA_PATH")

    private val MONGODB_FOODS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_FOODS_COLLECTION")
    private val MONGODB_RATINGS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_RATINGS_COLLECTION")
    private val MONGODB_TAGS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_TAGS_COLLECTION")

    private val ES_FOODS_INDEX: String = PropertiesUtil.getProperty("settings.properties", "ES_FOODS_INDEX")

    def main(args: Array[String]): Unit = {
        System.setProperty("es.set.netty.runtime.available.processors", "false")
        val conf: Map[String, String] = Map(
            "spark.cores" -> "local[*]",
            "mongo.uri" -> "mongodb://118.25.114.108/test",
            "mongo.db" -> "foodSystem",
            "es.httpHosts" -> "118.25.114.108:9200",
            "es.transportHosts" -> "118.25.114.108:9300",
            "es.index" -> "foods",
            "es.cluster.name" -> "elasticsearch"
        )

        val sparkConf: SparkConf = new SparkConf()
          .setMaster(conf("spark.cores"))
          .setAppName("DataLoad")

        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        spark.sparkContext.setLogLevel("ERROR")

        import spark.implicits._

        val SEPARATOR: String = PropertiesUtil.getProperty("settings.properties", "SEPARATOR")

        implicit val mongoConfig: MongoConf = MongoConf(conf("mongo.uri"), conf("mongo.db"))
        // ratings data
        val ratingsDF: DataFrame = spark.sparkContext.textFile(RATINGS_DATA_PATH)
          .map { data => {
              val fields: Array[String] = data.split(SEPARATOR)
              ratings(fields(0).toInt, fields(1).toInt, fields(2).toDouble, fields(3).toInt)
          }
          }.toDF()
        ratingsDF.show()

        // tags data
        val tagsDF: DataFrame = spark.sparkContext.textFile(TAGS_DATA_PATH)
          .map { data => {
              val fields: Array[String] = data.split(SEPARATOR)
              tags(fields(0).toInt, fields(1).toInt, fields(2), fields(3).toInt)
          }
          }.toDF()

        // 保存到mongoDB中
        implicit val mongoConf: MongoConf = MongoConf(conf("mongo.uri"), conf("mongo.db"))

        storeDataToMongoDB(ratingsDF, conf)(mongoConf)
//        storeDataToMongoDB(tagsDF, conf)
        // 保存到ES中

//        implicit val esConfig: ESConf = ESConf(
//            conf("es.httpHosts"),
//            conf("es.transportHosts"),
//            conf("es.index"),
//            conf("es.cluster.name"))
//        storeDataToES(foodES)

        spark.close()
    }

    def storeDataToMongoDB(ratingsDF: DataFrame, conf: Map[String, String])
                          (implicit mongoConf: MongoConf): Unit = {
        val mongoClient: MongoClient = MongoClient(MongoClientURI(mongoConf.uri))

        mongoClient(mongoConf.db)(MONGODB_RATINGS_COLLECTION).dropCollection()
//        mongoClient(mongoConf.db)(MONGODB_TAGS_COLLECTION).dropCollection()

        ratingsDF.write.mode("overwrite")
          .option("spark.mongodb.output.uri", conf("mongo.uri") + "." + MONGODB_RATINGS_COLLECTION)
          .format("com.mongodb.spark.sql").save()

//        tagsDF.write.mode("overwrite")
        ////          .option("spark.mongodb.output.uri", conf("mongo.uri") + "." + MONGODB_TAGS_COLLECTION)
        ////          .format("com.mongodb.spark.sql").save()

        mongoClient(mongoConf.db)(MONGODB_RATINGS_COLLECTION).createIndex(MongoDBObject("uid" -> 1))
        mongoClient(mongoConf.db)(MONGODB_RATINGS_COLLECTION).createIndex(MongoDBObject("fid" -> 1))

        mongoClient.close()
    }

    def storeDataToES(foodsWithTagsDF: DataFrame)(implicit esConf: ESConf): Unit = {
        val settings: Settings = Settings.builder().put("cluster.name", esConf.clusterName).build()
        val esClient: PreBuiltTransportClient = new PreBuiltTransportClient(settings)

        val REGEX_HOST_PORT: Regex = "(.+):(\\d+)".r
        esConf.transportPort.split(",").foreach {
            case REGEX_HOST_PORT(host: String, port: String) => {
                esClient.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port.toInt))
            }
        }

        if (esClient.admin().indices().exists(new IndicesExistsRequest(esConf.index)).actionGet().isExists) {
            esClient.admin().indices().delete(new DeleteIndexRequest(esConf.index))
        }

        esClient.admin().indices().create(new CreateIndexRequest(esConf.index))

        foodsWithTagsDF.write
          .option("es.nodes", esConf.HttpHost)
          .option("es.http.timeout", "100m")
          .option("es.mapping.id", "fid")
          .option("es.batch.write.retry.count", "10")
          .option("es.batch.write.retry.wait", "60")
          .mode("overwrite")
          .format("org.elasticsearch.spark.sql")
          .save(esConf.index + "/" + ES_FOODS_INDEX)

        esClient.close()

    }
}

case class Foods(fid: BigInt, name: String, describe: String, genres: String, price: String, types: String, avatar: String, canteen: String, _class: String)

case class ratings(uid: Int, fid: Int, score: Double, timestamp: Int)

case class tags(uid: Int, fid: Int, tag: String, timestamp: Int)

case class MongoConf(uri: String, db: String)

case class ESConf(HttpHost: String, transportPort: String, index: String, clusterName: String)
