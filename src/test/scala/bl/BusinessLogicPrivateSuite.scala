package bl

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class BusinessLogicPrivateSuite extends AnyFunSuite with ScalaCheckPropertyChecks {
  val bl = new BusinessLogic[Either[String, *], Seq]

  test("groupByKey empty seq returns empty seq") {
    bl.groupByKey(Seq()) shouldBe Seq()
  }

  test("groupByKey on nonempty seq groups properly") {
    bl.groupByKey(
      Seq(1 -> 10, 1 -> 11, 2 -> 20, 3 -> 30, 3 -> 31)
    ).sortBy(_._1) shouldBe
      Seq(
        1 -> List(10, 11),
        2 -> List(20),
        3 -> List(30, 31))
  }

  test("computeFrequencies on nonempty seq groups properly") {
    bl.computeFrequencies(
      Seq(
        1 -> 10,
        1 -> 11,
        1 -> 10,
        2 -> 20,
        1 -> 10,
        3 -> 30,
        3 -> 31)
    ).sortBy(x => (x._1, x._2)) shouldBe
      Seq(
        (1, 10, 3),
        (1, 11, 1),
        (2, 20, 1),
        (3, 30, 1),
        (3, 31, 1))
  }

  test("extractOddOcurringKvPairs works fine for single occuring pairs") {
    bl
      .extractPairsWithOddOccuringValues(
        Seq((1, 10, 3), (2, 20, 1), (3, 30, 1))
      ).map(_.sortBy(x => x._1 -> x._2)) shouldBe
      Right(Seq(1 -> 10, 2 -> 20, 3 -> 30))
  }

  test("extractOddOcurringKvPairs fails for multiple occuring pairs") {
    bl.extractPairsWithOddOccuringValues(
      Seq((1, 10, 3), (1, 11, 1), (2, 20, 1), (3, 30, 1))
    ) shouldBe
      Left("The key 1 has multiple odd occuring values")
  }
}