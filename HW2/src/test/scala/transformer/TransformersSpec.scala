package transformer

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TransformersSpec extends AnyFlatSpec with Matchers {

  "duplicate" should "duplicate string at the end of line" in {
    Transformers.duplicate(
      "Everyone get in here "
    ) shouldEqual "Everyone get in here Everyone get in here "
  }

  "divide" should "divide the line by half and return first half as the result" in {
    Transformers.divide("Even length text") shouldEqual "Even len"
    Transformers.divide("Odd length text") shouldEqual "Odd len"
  }

  "revert" should "return line in opposite direction" in {
    Transformers.revert("Tenet") shouldEqual "teneT"
  }

  "closure" should "closure string line and be able to call function on it after with result" in {
    val closure = Transformers.closure("Never odd or even")
    closure(Transformers.revert) shouldEqual "neve ro ddo reveN"
    closure(_ + "!") shouldEqual "Never odd or even!"
  }

}
