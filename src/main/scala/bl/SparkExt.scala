package bl

import org.apache.spark.sql.Dataset
import cats._
object SparkExt {

  implicit val dataSetTraverse: Traverse[Dataset] = ???

  implicit val datasetFunctorFilter: FunctorFilter[Dataset] = ???
}
