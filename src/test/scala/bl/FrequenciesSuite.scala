package bl

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class FrequenciesSuite extends AnyFunSuite {
  import StdLibExt._

  test("empty list yields empty frequencies") {
    List().frequencies shouldBe Map()
  }

  test("[1,1] -> {1 -> 2}") {
    List(1, 1).frequencies shouldBe Map(1 -> 2)
  }

  test("[1,2] -> {1 -> 1, 2->1}") {
    List(1, 2).frequencies shouldBe Map(1 -> 1, 2 -> 1)
  }
}
