package bl

object StdLibExt {
  implicit class SeqExt[A](s: Seq[A]) {
    /**
     * Computes the frequencies of aparition of distinct values in the input sequence
     */
    def frequencies: Map[A, Int] =
      s
        .groupBy(identity)
        .view
        .mapValues(_.size)
        .toMap

  }
}
