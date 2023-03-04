package bl
import cats._
import org.scalacheck.cats.implicits.genInstances
import org.scalacheck._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.util.Random

object Generators {

  val pairGen = Applicative[Gen].product(Gen.posNum[Int], Gen.posNum[Int])

  def smallNonEmptyList[T](g: Gen[T]) =
    Gen
      .choose(1, 5)
      .flatMap{size =>
        if (size == 0)
          println("size ZERO!!!")
        else ()

        Gen.listOfN(size, g)
      }

  val allOddOccurences =
    Applicative[Gen].product(Gen.choose(1,3), smallNonEmptyList(pairGen))
      .map { case(multiplyBy, lst) =>
        val distinctItems = lst.distinctBy(_._1)
        val multiplied =
          Seq.range(0, 2 * multiplyBy)
            .foldLeft(distinctItems){case (acc, _) => acc.concat(distinctItems)}

        Random.shuffle(multiplied)
      }

  val duplicatedList =
    smallNonEmptyList(pairGen)
      .map { lst => Random.shuffle(lst.concat(lst))}
}
class BusinessLogicSuite extends AnyFunSuite with ScalaCheckPropertyChecks {
  val bl = new BusinessLogic[Either[String, *], Seq]

  test("empty input -> should fail") {
    bl.processSeq(Seq()) shouldBe Left("Some values occur an even number of times")
  }

  test("multiple odd values for the same key -> error") {
    bl.processSeq(
      Seq(
        1 -> 1,
        1 -> 2
      )
    ) shouldBe Left("The key 1 has multiple odd occuring values")
  }

  test("no odd values for the same key -> error") {
    bl.processSeq(
      Seq(
        1 -> 10,
        1 -> 10
      )
    ) shouldBe Left("Some values occur an even number of times")
  }

  test("multiple keys with odd occuring values") {
    bl.processSeq(
      Seq(
        1 -> 1,
        2 -> 2,
        2 -> 2,
        2 -> 2
      )
    ) shouldBe Right(Seq(1 -> 1, 2 -> 2))
  }

  test("qc1") {
    bl.processSeq(
      Seq((9, 6), (3, 6), (2, 10), (9, 6), (3, 6), (2, 10), (9, 6), (3, 6), (2, 10))
    ) shouldBe Right(Seq((9, 6), (2, 10), (3, 6)))
  }

  test("qc2") {
    bl.processSeq(
      List((3, 6), (2, 10), (9, 6), (3, 6), (2, 10), (9, 6), (3, 6), (2, 10))
    ) shouldBe Left("Some values occur an even number of times")
  }

  test("processSeq idempotence") {
    forAll { (lst: List[(Int, Int)]) =>
      bl.processSeq(lst).flatMap(bl.processSeq) shouldBe bl.processSeq(lst)
    }
  }

  test("processSeq succeeds for valid input") {
    forAll(Generators.allOddOccurences) { (lst: List[(Int, Int)]) =>
      bl.processSeq(lst).isRight shouldBe true
    }
  }

  test("processSeq fails for valid input with odd items removed") {
    forAll(Generators.allOddOccurences, Gen.posNum[Int]) { (lst: List[(Int, Int)], n : Int) =>
      bl.processSeq(lst.tail) shouldBe Left("Some values occur an even number of times")
    }
  }

  test("processSeq fails for duplicated lists") {
    forAll(Generators.duplicatedList) { (lst: List[(Int, Int)]) =>
      bl.processSeq(lst) shouldBe Left("Some values occur an even number of times")
    }
  }
}