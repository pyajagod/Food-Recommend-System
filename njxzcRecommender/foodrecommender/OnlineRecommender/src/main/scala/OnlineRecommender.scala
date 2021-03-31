import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI, MongoCollection}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import redis.clients.jedis.Jedis
import utils.PropertiesUtil

object OnlineRecommender {
    private val MAX_USER_RATINGS_NUM: String = PropertiesUtil.getProperty("settings.properties", "MAX_USER_RATINGS_NUM")
    private val MAX_SIM_FOODS_NUM: String = PropertiesUtil.getProperty("settings.properties", "MAX_SIM_FOODS_NUM")

    private val MONGODB_FOODS_RECS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_FOODS_RECS_COLLECTION")
    private val MONGODB_RATING_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_RATING_COLLECTION")

    private val MONGODB_STREAM_RECS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_STREAM_RECS_COLLECTION")

    private val SEPARATOR: String = PropertiesUtil.getProperty("settings.properties", "SEPARATOR")


    def main(args: Array[String]): Unit = {
        val config: Map[String, String] = Map(
            "spark.cores" -> "local[*]",
//            "redis.address" -> "118.25.114.108",
            "mongo.uri" -> "mongodb://118.24.32.76/foodSystem",
            "mongo.db" -> "foodSystem",
            "kafka.topic" -> "NjxzcRecommender"
        )

        object ConnectHelper extends Serializable {
//            lazy val jedis: Jedis = new Jedis(config("redis.address"))
            lazy val mongoClient: MongoClient = MongoClient(MongoClientURI(config("mongo.uri")))
        }

        val sparkConf: SparkConf = new SparkConf()
        sparkConf.setAppName("OnlineRecommender")
        sparkConf.setMaster(config("spark.cores"))
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        val sc: SparkContext = spark.sparkContext
        val ssc: StreamingContext = new StreamingContext(sc, Seconds(2))
        spark.sparkContext.setLogLevel("ERROR")

        implicit val mongoConfig: MongoConf = MongoConf(config("mongo.uri"), config("mongo.db"))

        import spark.implicits._

        // food相似度矩阵
        val simFoodsMatrix: collection.Map[Int, Map[Int, Double]] = spark.read
          .option("spark.mongodb.input.uri", mongoConfig.uri + "." + MONGODB_RATING_COLLECTION)
          .option("collection", MONGODB_FOODS_RECS_COLLECTION)
          .format("com.mongodb.spark.sql")
          .load()
          .as[FoodRecommendations]
          .rdd
          .map {
              foodRecommend => (foodRecommend.fid, foodRecommend.recs.map(x => (x.fid, x.score)).toMap)
          }
          .collectAsMap()

        val simFoodMatrixBroadCast: Broadcast[collection.Map[Int, Map[Int, Double]]] = sc.broadcast(simFoodsMatrix)

        // 创建kafka连接
        val kafkaParam = Map(
            "bootstrap.servers" -> "118.24.32.76:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> "0",
            "auto.offset.reset" -> "latest"
        )
        val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
            ssc,
            LocationStrategies.PreferConsistent,
            ConsumerStrategies.Subscribe[String, String](Array(config("kafka.topic")), kafkaParam)
        )

        // 处理kafka收集过来的数据
        val ratingsStream: DStream[(Int, Int, Double, Int)] = kafkaStream.map {
            msg =>
                val fields: Array[String] = msg.value().split(SEPARATOR)
                (fields(0).toInt, fields(1).toInt, fields(2).toDouble, fields(3).toInt)
        }

        ratingsStream.foreachRDD {
            rdds => {
                rdds.foreach {
                    case (uid, fid, score, timestamp) => {
                        println("data is coming!!!")

//                        // 得到user最新评论得二十条数据
//                        val userRecentlyRatings: Array[(Int, Double)] =
//                            getUserRecentlyRatings(MAX_USER_RATINGS_NUM.toInt, uid, ConnectHelper.jedis)

                        // 得到与用户评论food相似度最高的前二十条数据
                        val streamRecs: Array[(Int, Double)] =
                            getTopSimFoods(MAX_SIM_FOODS_NUM.toInt, fid, uid, simFoodMatrixBroadCast.value, ConnectHelper.mongoClient)

                        storeDataInMongoDB(uid, streamRecs, ConnectHelper.mongoClient)
                    }
                }
            }
        }

        ssc.start()
        println("SparkStreaming keep going!!!")
        ssc.awaitTermination()
    }

    import scala.collection.JavaConversions._

//    def getUserRecentlyRatings(num: Int, uid: Int, jedis: Jedis): Array[(Int, Double)] = {
//        jedis.lrange("uid:" + uid, 0, num).map {
//            data => {
//                val fields: Array[String] = data.split(SEPARATOR)
//                (fields(0).trim.toInt, fields(1).trim.toDouble)
//            }
//        }.toArray
//    }

    def getTopSimFoods(num: Int, fid: Int, uid: Int, simFoods: collection.Map[Int, Map[Int, Double]], mongoClient: MongoClient)
                      (implicit mongoConf: MongoConf): Array[(Int, Double)] = {
        val allSimFoods: Array[(Int, Double)] = simFoods(fid).toArray // fid的food和其他food的相似度
        if (allSimFoods == null) {
            System.exit(0)
        }
        val ratingExist: Array[Int] = mongoClient(mongoConf.db)(MONGODB_RATING_COLLECTION)
          .find(MongoDBObject("uid" -> uid))
          .toArray
          .map {
              item => item.get("fid").toString.toInt
          }
        println("this is allSimFoods:")
        allSimFoods.foreach(println)

        allSimFoods.sortWith(_._2 > _._2)
          .take(num)
          .map(x => (x._1, x._2))
    }

    //    def computeFoodsScores(candidateFoods: Array[Int], userRecentlyRatings: Array[(Int, Double)],
    //                           simFoods: scala.collection.Map[Int, scala.collection.immutable.Map[Int, Double]]): Array[(Int, Double)] = {
    //        val scores: ArrayBuffer[(Int, Double)] = collection.mutable.ArrayBuffer[(Int, Double)]()
    //        val increaseMap: mutable.HashMap[Int, Int] = collection.mutable.HashMap[Int, Int]()
    //        val decreaseMap: mutable.HashMap[Int, Int] = collection.mutable.HashMap[Int, Int]()
    //        for (candidateFood <- candidateFoods; userRecentlyRating <- userRecentlyRatings) {
    //            val simScore: Double = getFoodsSimScore(candidateFood, userRecentlyRating._1, simFoods)
    //            println(simScore)
    //            if (simScore > 0.6) {
    //                scores += ((candidateFood, simScore * userRecentlyRating._2))
    //                if (userRecentlyRating._2 > 3) {
    //                    increaseMap(candidateFood) = increaseMap.getOrDefault(candidateFood, 0) + 1
    //                } else {
    //                    decreaseMap(candidateFood) = decreaseMap.getOrDefault(candidateFood, 0) + 1
    //                }
    //            }
    //        }
    //        scores.groupBy(_._1).map {
    //            case (fid, scoreList) => {
    //                (fid, scoreList.map(_._2).sum /
    //                  scoreList.length + log(increaseMap.getOrDefault(fid, 1)) - log(decreaseMap.getOrDefault(fid, 1)))
    //            }
    //        }
    //          .toArray
    //          .sortWith(_._2 > _._2)
    //    }
    //
    //    def getFoodsSimScore(fid1: Int, fid2: Int, simFoods: scala.collection.Map[Int, scala.collection.immutable.Map[Int, Double]]): Double = {
    //        simFoods.get(fid1) match {
    //            case Some(sims) => {
    //                sims.get(fid2)
    //            } match {
    //                case Some(score) => score
    //                case None => 0.0
    //            }
    //            case None => 0.0
    //        }
    //    }
    //
    //    def log(i: Int): Double = {
    //        val N = 10
    //        math.log(i) / math.log(N)
    //    }

    def storeDataInMongoDB(uid: Int, streamRecs: Array[(Int, Double)], mongoClient: MongoClient)(implicit mongoConf: MongoConf): Unit = {
        val streamRecsCollection: MongoCollection = mongoClient(mongoConf.db)(MONGODB_STREAM_RECS_COLLECTION)
        streamRecsCollection.findAndRemove(MongoDBObject("uid" -> uid))
        streamRecsCollection.insert(MongoDBObject("uid" -> uid,
            "recs" -> streamRecs.map(x => MongoDBObject("fid" -> x._1, "score" -> x._2))))
    }
}

case class Recommendation(fid: Int, score: Double)

case class UserRecommendations(uid: Int, recs: Seq[Recommendation])

case class FoodRecommendations(fid: Int, recs: Seq[Recommendation])

case class MongoConf(uri: String, db: String)
