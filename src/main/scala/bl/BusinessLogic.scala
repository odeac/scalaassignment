package bl

import cats._
import cats.syntax.all._
import cats.implicits._

/**
 * Implementation of the business logic. It's polymorphic in the effect that it produces
 *
 * This is work in progress.
 */
class BusinessLogic[M[_], S[_]](implicit
                                monadError: MonadError[M, String],
                                foldable : Foldable[S],
                                functorFilter : FunctorFilter[S]) {

  import StdLibExt._

  type Key = Int
  type Value = Int

  private[bl] def groupByKey(seq: S[(Key, Value)]): S[(Key, S[Value])] =
    seq
      .groupBy(_._1)
      .map { case (k, values) => (k, values.map(_._2)) }

  /**
   * given a pair of key -> frequencies_map, returns a pair key -> sequence containing the odd occurring values.
   * Fails if the number of odd ocurrences is != 1
   */
  private[bl] def extractPairsWithOddOccuringValues(input: S[(Key, Value, Int)]): M[S[(Key, Value)]] = {
    val elemsWithEvenFreqs = input.filter { case (_, _, freq) => freq % 2 == 0 }

    if (input.isEmpty || elemsWithEvenFreqs.nonEmpty)
      monadError.raiseError("Some values occur an even number of times")
    else
      input.groupBy(_._1)
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
  private[bl] def computeFrequencies(seq: S[(Key, Value)]): S[(Key, Value, Int)] =
    groupByKey(seq)
      .flatMap({
        case (key, values) =>
          values.frequencies
            .map { case (value, freq) => (key, value, freq) }
      })

  def processSeq(seq: S[(Key, Value)]): M[S[(Key, Value)]] = {
    extractPairsWithOddOccuringValues(computeFrequencies(seq))
  }
}
