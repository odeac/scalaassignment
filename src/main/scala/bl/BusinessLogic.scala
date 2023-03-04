package bl

import cats.MonadError
import cats.implicits._

/**
 * Implementation of the business logic. It's polymorphic in the effect that it produces
 */
class BusinessLogic[M[_]](implicit monadError: MonadError[M, String]) {

  import StdLibExt._

  type Key = Int
  type Value = Int

  private[bl] def groupByKey(seq: Seq[(Key, Value)]): Seq[(Key, Seq[Value])] =
    seq
      .groupBy(_._1)
      .toSeq
      .map { case (k, values) => (k, values.map(_._2)) }

  /**
   * given a pair of key -> frequencies_map, returns a pair key -> sequence containing the odd occurring values.
   * Fails if the number of odd ocurrences is != 1
   */
  private[bl] def extractPairsWithOddOccuringValues(input: Seq[(Key, Value, Int)]): M[Seq[(Key, Value)]] = {
    val elemsWithEvenFreqs = input.filter { case (_, _, freq) => freq % 2 == 0 }

    if (input.isEmpty || elemsWithEvenFreqs.nonEmpty)
      monadError.raiseError("Some values occur an even number of times")
    else
      input.groupBy(_._1)
        .toSeq
        //      .map{case kv => kv}
        .traverse {
          case (key, Seq((_, value, _))) =>
            monadError.pure(key -> value)
          case (key, _) =>
            monadError.raiseError[(Key, Value)](s"The key $key has multiple odd occuring values")
        }
  }

  /**
   * For each key-value pair, adds information about the frequency of its values
   */
  private[bl] def computeFrequencies(seq: Seq[(Key, Value)]): Seq[(Key, Value, Int)] =
    groupByKey(seq)
      .flatMap({
        case (key, values) =>
          values.frequencies
            .toSeq
            .map { case (value, freq) => (key, value, freq) }
      })

  def processSeq(seq: Seq[(Key, Value)]): M[Seq[(Key, Value)]] = {
    extractPairsWithOddOccuringValues(computeFrequencies(seq))
  }
}
