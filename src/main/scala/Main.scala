
import bl.BusinessLogic
import org.apache.spark.sql.functions.monotonically_increasing_id
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import cats._
import cats.data.EitherT
import cats.implicits._
import cats.syntax.all._
import frameless.cats._
import frameless.syntax._
object Main {

  private def parseEntry(row: Row): Either[String, (Int, Int)] = {
    import scala.util.Try
    import cats.syntax.all._

    def parseField(fieldName: String, index: Int) =
      for {
        s <- Try {
          row.getAs[String](index)
        }.toEither.leftMap(_.getMessage)

        v <- if (s.isEmpty)
          Right(0)
        else
          s.toIntOption
            .toRight(s"Cannot parse $fieldName from $s")
      } yield v


    lazy val rowId = row.getAs[String](0)

    if (row.length != 3)
      Left(s"Cannot parse row $rowId")
    else
      for {
        key <- parseField("key", 1)
        value <- parseField("value", 2)
      } yield key -> value
  }

  def main(args: Array[String]) = {
    args match {
      case Array(inputDir, outputFile) =>
        System.out.println(s"Input dir:'$inputDir\nOutput file:'$outputFile''")

        val spark = SparkSession.builder().appName("Vigil test").master("local").getOrCreate()
        import spark.implicits._
        val input = spark.read.options(Map("sep" -> "\t,", "header" -> "true")).csv(inputDir)
        import scala.jdk.CollectionConverters._

        val businessLogic = new BusinessLogic[Either[String, *], Dataset]

        val result = for {
          dataset <- input
          .withColumn("index", monotonically_increasing_id())
          .traverse(parseEntry)
          result <- dataset.traverse(businessLogic.processSeq)
        } yield result

        result.fold(
          err => System.err.println(s"Invalid input: $err"),
          result => {
            System.out.println("Printing results...")
            val df = result.toDF("key", "value")
            df.write.csv(outputFile)
            System.out.println("Done.")
          }
        )
        spark.stop()
      case _ =>
        println(
          s"""
             | Invalid command line parameters: {${args.mkString(",")}}
             | Usage: vigil-test <input_dif> <output_file>
             | input_path - directory where the input files are found
             | output_path - directory where the output files are found
             |""".stripMargin)
        System.exit(1)
    }
  }
}
