import java.text.SimpleDateFormat
import java.util.Date

import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.PropertiesUtil

object StaticsRecommender {
    private val MONGODB_FOODS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_FOODS_COLLECTION")
    private val MONGODB_RATINGS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_RATINGS_COLLECTION")

    private val History_Rate_Foods: String = PropertiesUtil.getProperty("settings.properties", "HISTORY_RATE_FOODS")
    private val Recently_Rate_Foods: String = PropertiesUtil.getProperty("settings.properties", "RECENTLY_RATE_FOODS")
    private val Average_Score_Foods: String = PropertiesUtil.getProperty("settings.properties", "AVERAGE_SCORE_FOODS")
    private val TypeTopFoods: String = PropertiesUtil.getProperty("settings.properties", "tYPE_TOP_FOODS")

    private val NUMS: String = PropertiesUtil.getProperty("settings.properties", "NUMS")

    def main(args: Array[String]): Unit = {
        val config: Map[String, String] = Map(
            "spark.cores" -> "local[1]",
            "mongo.uri" -> "mongodb://118.24.32.76/test",
            "mongo.db" -> "foodSystem"
        )

        val sparkConf: SparkConf = new SparkConf()
          .setMaster(config("spark.cores"))
          .setAppName("StaticsRecommender")

        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        spark.sparkContext.setLogLevel("ERROR")
        import spark.implicits._

        implicit val mongoConfig: MongoConf = MongoConf(config("mongo.uri"), config("mongo.db"))

        val foodsDF: DataFrame = spark.read.option("spark.mongodb.input.uri", mongoConfig.uri + "." + MONGODB_FOODS_COLLECTION)
          .format("com.mongodb.spark.sql")
          .load()
          .as[Foods]
          .toDF()

        //        foodsDF.createOrReplaceTempView("foods")

        val ratingsDF: DataFrame = spark.read.option("spark.mongodb.input.uri", mongoConfig.uri + "." + MONGODB_RATINGS_COLLECTION)
          .format("com.mongodb.spark.sql")
          .load()
          .as[ratings]
          .toDF()


        ratingsDF.createOrReplaceTempView("ratings")

        // 1、历史评分最多的food
        val rateMoreFoodsDF: DataFrame = spark.sql(
            "select fid, count(fid) as count from ratings group by fid order by count desc"
        )
        val historyRateFoods: DataFrame = foodsDF.join(rateMoreFoodsDF, "fid")
        storeDataInMongoDB(historyRateFoods, History_Rate_Foods)

        // 2、最近最多评论的food
        val simpleDataFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMM")
        spark.udf.register("changeDate", (x: Int) => simpleDataFormat.format(new Date(x * 1000L)).toInt)
        val ratingsYearMonth: DataFrame = spark.sql("select fid, score, changeDate(timestamp) as yearMonth from ratings")
        ratingsYearMonth.createOrReplaceTempView("ratingsYearMonth")
        val rateMoreRecentlyFoods: DataFrame = spark.sql("select " +
          "fid, count(fid) as count, yearMonth " +
          "from ratingsYearMonth " +
          "group by yearMonth, fid " +
          "order by yearMonth desc, count desc")
        val RecentlyRateFoods: DataFrame = foodsDF.join(rateMoreRecentlyFoods, "fid")
        //        storeDataInMongoDB(RecentlyRateFoods, Recently_Rate_Foods)
        //
        //        // 3、平均评分得分
        val averageFoods: DataFrame = spark.sql("select fid, avg(score) as avgScore from ratings group by fid")
        val AverageScoreFoods: DataFrame = foodsDF.join(averageFoods, "fid")
//        AverageScoreFoods.show()
        //        storeDataInMongoDB(AverageScoreFoods, Average_Score_Foods)
        //
        // 4、八大菜系中菜品平均评分topN
        val types: List[String] = List("sichuan", "anhui", "shandong", "guangdong", "fujian", "jiangsu", "zhejian", "hunan")
        val typesRDD: RDD[String] = spark.sparkContext.makeRDD(types)

        val foodWithAvgScore: DataFrame = foodsDF.join(averageFoods, "fid")
        val typesTopFoods: DataFrame = typesRDD.cartesian(AverageScoreFoods.rdd)
          .filter {
              case (types, foodRow) => foodRow.getAs[String]("types").toLowerCase().contains(types)
          }
          .map {
              case (types, foodRow) => (types, (foodRow.getAs[Long]("fid"), foodRow.getAs[Double]("avgScore")))
          }
          .groupByKey()
          .map {
              case (genre, item) =>
                  TypesRecommendation(genre, item.toList.sortWith(_._2 > _._2).take(NUMS.toInt).map(x => Recommendation(x._1, x._2)))
          }
          .toDF()
//        typesTopFoods.show()
//        storeDataInMongoDB(typesTopFoods, TypeTopFoods)

        spark.close()
    }

    def storeDataInMongoDB(df: DataFrame, collectionName: String)(implicit mongoConf: MongoConf): Unit = {
        val mongoClient: MongoClient = MongoClient(MongoClientURI(mongoConf.uri))
        mongoClient(mongoConf.db)(collectionName).dropCollection()

        df.write.mode("overwrite")
          .option("spark.mongodb.output.uri", mongoConf.uri + "." + collectionName)
          .format("com.mongodb.spark.sql").save()

        mongoClient.close()
    }
}

case class Foods(fid: BigInt, name: String, describe: String, genres: String, price: String, types: String, avatar: String, canteen: String, _class: String)

case class ratings(uid: Int, fid: Int, score: Double, timestamp: Int)

case class tags(uid: Int, fid: Int, tag: String, timestamp: Int)

case class Recommendation(fid: Long, score: Double)

case class TypesRecommendation(genres: String, recs: Seq[Recommendation])

case class MongoConf(uri: String, db: String)

case class ESConf(HttpHost: String, transportPort: String, index: String, clusterName: String)
