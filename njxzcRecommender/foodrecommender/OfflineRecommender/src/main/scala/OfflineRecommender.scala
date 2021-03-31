import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.jblas.DoubleMatrix
import utils.PropertiesUtil

object OfflineRecommender {
    private val MONGODB_RATINGS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_RATINGS_COLLECTION")
    private val MONGODB_FOODS_COLLECTION: String = PropertiesUtil.getProperty("settings.properties", "MONGODB_FOODS_COLLECTION")

    private val USER_RECS: String = PropertiesUtil.getProperty("settings.properties", "USER_RECS")
    private val FOOD_RECS: String = PropertiesUtil.getProperty("settings.properties", "FOOD_RECS")

    private val USER_MAX_RECOMMENDATION: String = PropertiesUtil.getProperty("settings.properties", "USER_MAX_RECOMMENDATION")

    def main(args: Array[String]): Unit = {
        val config: Map[String, String] = Map(
            "spark.cores" -> "local[*]",
            "mongo.uri" -> "mongodb://118.24.32.76/test",
            "mongo.db" -> "foodSystem"
        )

        val sparkConf: SparkConf = new SparkConf()
        sparkConf.setAppName("OfflineRecommender")
        sparkConf.setMaster(config("spark.cores"))
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        spark.sparkContext.setLogLevel("ERROR")

        implicit val mongoConfig: MongoConf = MongoConf(config("mongo.uri"), config("mongo.db"))

        import spark.implicits._

        val ratingsRDD: RDD[(Int, Int, Double)] = spark.read.option("spark.mongodb.input.uri", mongoConfig.uri + "." + MONGODB_RATINGS_COLLECTION)
          .format("com.mongodb.spark.sql")
          .load()
          .as[FoodRatings]
          .rdd
          .map {
              fields => (fields.uid, fields.fid, fields.score)
          }
          .cache()

        val foodsDF: DataFrame = spark.read.option("spark.mongodb.input.uri", mongoConfig.uri + "." + MONGODB_FOODS_COLLECTION)
          .format("com.mongodb.spark.sql")
          .load()
          .as[Foods]
          .toDF()

        foodsDF.createOrReplaceTempView("foods")

        // 创建空矩阵
        val userRDD: RDD[Int] = ratingsRDD.map(_._1).distinct()
        val foodRDD: RDD[Int] = ratingsRDD.map(_._2).distinct()
        val userFoods: RDD[(Int, Int)] = userRDD.cartesian(foodRDD)

        // 得到训练的数据集
        val trainData: RDD[Rating] = ratingsRDD.map(rating => Rating(rating._1, rating._2, rating._3))
        val (rank, iterations, lambda) = (50, 5, 0.01)
        val model: MatrixFactorizationModel = ALS.train(trainData, rank, iterations, lambda)

        // 预测评分
        val preRatings: RDD[Rating] = model.predict(userFoods)

        val userRecs: DataFrame = preRatings.filter(_.rating > 0)
          .map(rating => (rating.user, (rating.product, rating.rating)))
          .groupByKey()
          .map {
              case (uid, item) =>
                  UserRecommendations(uid, item.toList.sortWith(_._2 > _._2).take(USER_MAX_RECOMMENDATION.toInt)
                    .map(x => {
                        Recommendation(x._1, x._2)
                    }))
          }
          .toDF()


        //        storeDataInMongoDB(userRecs, USER_RECS)

        // 得到food的特征矩阵
        val foodFeatures: RDD[(Int, DoubleMatrix)] = model.productFeatures.map {
            case (mid, features) => (mid, new DoubleMatrix(features))
        }

        // food特征矩阵做笛卡尔积，得到两两food的相似度
        val foodRecs: DataFrame = foodFeatures.cartesian(foodFeatures)
          .filter {
              case (a, b) => {
                  a._1 != b._1
              }
          }
          .map {
              case (a, b) => {
                  val simScore: Double = cosineSim(a._2, b._2)
                  (a._1, (b._1, simScore))
              }
          }
          .filter(_._2._2 > 0.6)
          .groupByKey()
          .filter{
              case(fid, items)=> fid>0
          }
          .map {
              case (fid, items) =>
                  FoodReommendations(fid, items.toList.sortWith(_._2 > _._2).map(x => Recommendation(x._1, x._2)))
          }
          .toDF()

        //        storeDataInMongoDB(foodRecs, FOOD_RECS)
        foodRecs.show()
        spark.close()
    }

    // 余弦相似度计算
    def cosineSim(food1: DoubleMatrix, food2: DoubleMatrix): Double = {
        food1.dot(food2) / (food1.norm2() * food2.norm2())
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

case class Foods(fid: BigInt, name: String, describe: String, genres: String, price: String, types: String, avatar: String,
                 canteen: String, _class: String)

case class FoodRatings(uid: Int, fid: Int, score: Double, timestamp: Int)

case class Tags(uid: Int, fid: Int, tag: String, timestamp: Int)

case class Recommendation(fid: Int, score: Double)

case class UserRecommendations(uid: Int, recs: Seq[Recommendation])

case class FoodReommendations(fid: Int, recs: Seq[Recommendation])

case class MongoConf(uri: String, db: String)

case class ESConf(HttpHost: String, transportPort: String, index: String, clusterName: String)
