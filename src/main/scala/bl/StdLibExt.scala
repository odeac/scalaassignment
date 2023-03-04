package bl

import cats._
import cats.syntax.all._
import cats.implicits._

import scala.collection.Map

object StdLibExt {
  implicit class FoldableOps[M[_], A](seq: M[A])
                                     (implicit
                                      monad: Monad[M],
                                      foldable: Foldable[M]) {
    /**
     * Groups the items in M by a given projection.
     *
     * @param projection
     * @tparam A
     * @tparam B
     */
    def groupBy[K](projection: A => K)(implicit monoid: Monoid[M[(K, List[A])]]): M[(K, List[A])] = {
      val accumulator: (Map[K, List[A]], A) => Map[K, List[A]] = {
          case (acc, a) =>
            val k = projection(a)
            if (acc.isEmpty) Map(k -> List(a))
            else acc.get(k) match {
              case Some(values) => (acc - k) + (k -> a :: values)
              case None => acc + (k -> List(a))
            }
        }

      val groups: Map[K, List[A]] =
        seq.foldl(Map[K, List[A]]())(accumulator)

      val r = groups.foldLeft(monoid.empty) {
        case (acc, (key, values)) => monoid.combine(acc, monad.pure((key, values)))
      }
      r
    }

    def length = seq.foldl(0) { case (acc, _) => acc + 1 }

    /**
     * Computes the frequencies of aparition of distinct values in the input sequence
     */
    def frequencies: M[(A, Int)] =
      seq
        .groupBy(identity)
        .map(pair => (pair._1, pair._2.length))
  }
}
