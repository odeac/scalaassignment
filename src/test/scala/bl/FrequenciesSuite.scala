package bl

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper


class FrequenciesSuite extends AnyFunSuite {
  import StdLibExt._

  test("empty list yields empty frequencies") {
    List().frequencies shouldBe List()
  }

  test("[1,1] -> {1 -> 2}") {
    List(1, 1).frequencies shouldBe List(1 -> 2)
  }

  test("[1,2] -> {1 -> 1, 2->1}") {
    List(1, 2).frequencies shouldBe List(1 -> 1, 2 -> 1)
  }
}
