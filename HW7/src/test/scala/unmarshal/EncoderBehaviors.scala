package unmarshal

import data.{CompanyEmployee, Employee}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import unmarshal.TestSample.{employee1242, employee1242Json, employee1489, employee1489Json}
import unmarshal.encoder.Encoder
import unmarshal.model.Json.{JsonArray, JsonObject}

trait EncoderBehaviors { this: AnyFlatSpec with Matchers =>

  def employeeEncoder(encoder: => Encoder[Employee]): Unit = {

    it should "correctly encode Employee" in {
      encoder.toJson(employee1242) shouldBe employee1242Json
    }

    it should "correctly encode Employee with non empty boss id" in {
      encoder.toJson(employee1489) shouldBe employee1489Json
    }
  }

  def companyEmployeeEncoder(encoder: => Encoder[CompanyEmployee]): Unit = {

    it should "correctly encoder CompanyEmployee" in {
      encoder.toJson(CompanyEmployee(List(employee1242))) shouldBe
        JsonObject(Map("employees" -> JsonArray(List(employee1242Json))))
    }

    it should "save same order for Employee in CompanyEmployee" in {
      encoder.toJson(CompanyEmployee(List(employee1242, employee1489))) shouldBe
        JsonObject(Map("employees" -> JsonArray(List(employee1242Json, employee1489Json))))
    }

    it should "correctly encoder empty CompanyEmployee" in {
      encoder.toJson(CompanyEmployee(List())) shouldBe
        JsonObject(Map("employees" -> JsonArray(List())))
    }
  }

}
