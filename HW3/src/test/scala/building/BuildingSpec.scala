package building

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BuildingSpec extends AnyFlatSpec with Matchers {

  // specs on constructor validations

  "Resident" should "be invalid if age is non positive" in {
    1 shouldEqual 0 // Implement yourself
  }

  it should "be invalid if sex is non binary" in {
    1 shouldEqual 0 // Implement yourself
  }

  // specs on methods

  "countOldManFloors" should "return the number of men older than the specified age" in {
    1 shouldEqual 0 // Implement yourself
  }

  it should "return 0 if there are no men older than the specified age" in {
    1 shouldEqual 0 // Implement yourself
  }

  it should "return 0 if there are no men at all in the building" in {
    1 shouldEqual 0 // Implement yourself
  }

  "womanMaxAge" should "find age of the oldest woman in the building" in {
    1 shouldEqual 0 // Implement yourself
  }

  it should "return None if there are no women in the building" in {
    1 shouldEqual 0 // Implement yourself
  }

  "countCommercial" should "return number of commercial establishments in the building" in {
    1 shouldEqual 0 // Implement yourself
  }

  it should "return 0 if there are no commercial establishments in the building" in {
    1 shouldEqual 0 // Implement yourself
  }

  "countCommercialAvg" should "return average commercial establishments through array of buildings" in {
    1 shouldEqual 0 // Implement yourself
  }

  it should "return 0 if there are no commercial establishments in the buildings" in {
    1 shouldEqual 0 // Implement yourself
  }

  it should "return 0 if there are no buildings" in {
    1 shouldEqual 0 // Implement yourself
  }

  "evenFloorsMenAvg" should "return average count of mens on even floors in the building" in {
    1 shouldEqual 0 // Implement yourself
  }

  it should "return 0 if there are no mens in the building" in {
    1 shouldEqual 0 // Implement yourself
  }

}
