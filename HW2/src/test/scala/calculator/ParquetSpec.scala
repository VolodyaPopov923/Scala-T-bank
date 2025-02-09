package calculator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ParquetSpec extends AnyFlatSpec with Matchers {

  private val economy1 = Building.Economy(10, 25, 4, 2)
  private val economy2 = Building.Economy(23, 50, 3, 1)
  private val premium1 = Building.Premium(10, 25, 4, 2)
  private val premium2 = Building.Premium(23, 50, 4, 7)

  "calculate" should "calculate parquet cost correctly for economy building" in {
    Parquet.calculate(economy1) shouldEqual 21_000
    Parquet.calculate(economy2) shouldEqual 13_450
  }

  it should "calculate parquet cost corerectly for premium building" in {
    Parquet.calculate(premium1) shouldEqual 351
    Parquet.calculate(premium2) shouldEqual 9856
  }

}
