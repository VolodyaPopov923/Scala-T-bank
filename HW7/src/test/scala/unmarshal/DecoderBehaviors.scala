package unmarshal

import data.{CompanyEmployee, Employee}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import unmarshal.decoder.Decoder
import unmarshal.model.Json.{JsonArray, JsonBool, JsonNull, JsonNum, JsonObject, JsonString}

trait DecoderBehaviors { this: AnyFlatSpec with Matchers =>

  def employeeDecoder(decoder: => Decoder[Employee]): Unit = {

    it should "correctly decode Employee" in {
      decoder.fromJson(
        JsonObject(
          Map(
            "name"   -> JsonString("Jon"),
            "age"    -> JsonNum(42),
            "id"     -> JsonNum(1242),
            "bossId" -> JsonNull
          )
        )
      ) shouldBe Right(Employee("Jon", 42, 1242, None))
    }

    it should "return error if field not found" in {
      decoder.fromJson(
        JsonObject(
          Map(
            "name"   -> JsonString("Jon"),
            "id"     -> JsonNum(1242),
            "bossId" -> JsonNull
          )
        )
      ) match {
        case Left(error)  => error.field shouldBe "age"
        case Right(value) => fail(s"Expected error, but was $value")
      }
    }

    it should "return error for unknown field" in {
      decoder.fromJson(
        JsonObject(
          Map(
            "name"   -> JsonString("Jon"),
            "age"    -> JsonNum(42),
            "id"     -> JsonNum(1242),
            "bossId" -> JsonNull,
            "gender" -> JsonBool(true)
          )
        )
      ) match {
        case Left(error)  => error.field shouldBe "gender"
        case Right(value) => fail(s"Expected error, but was $value")
      }
    }

    it should "return error for wrong type" in {
      decoder.fromJson(
        JsonObject(
          Map(
            "name"   -> JsonString("Jon"),
            "age"    -> JsonNum(42),
            "id"     -> JsonNum(1242),
            "bossId" -> JsonBool(true)
          )
        )
      ) match {
        case Left(error)  => error.field shouldBe "bossId"
        case Right(value) => fail(s"Expected error, but was $value")
      }
    }
  }

  def companyEmployeeDecoder(decoder: => Decoder[CompanyEmployee]): Unit = {

    it should "correctly decode CompanyEmployee" in {
      val employee = JsonObject(
        Map(
          "name"   -> JsonString("Jon"),
          "age"    -> JsonNum(42),
          "id"     -> JsonNum(1242),
          "bossId" -> JsonNum(7)
        )
      )

      decoder.fromJson(
        JsonObject(Map("employees" -> JsonArray(List(employee))))
      ) shouldBe Right(CompanyEmployee(List(Employee("Jon", 42, 1242, Some(7)))))
    }

    it should "correctly decode empty CompanyEmployee" in {
      decoder.fromJson(
        JsonObject(Map("employees" -> JsonArray(List())))
      ) shouldBe Right(CompanyEmployee(List()))
    }

    it should "return error if nested field not found" in {
      val employee = JsonObject(
        Map(
          "name"   -> JsonString("Jon"),
          "id"     -> JsonNum(1242),
          "bossId" -> JsonNull
        )
      )

      decoder.fromJson(
        JsonObject(Map("employees" -> JsonArray(List(employee))))
      ) match {
        case Left(error)  => error.field shouldBe "employees.0.age"
        case Right(value) => fail(s"Expected error, but was $value")
      }
    }

    it should "return error if expected type A, but was null" in {
      decoder.fromJson(
        JsonObject(Map("employees" -> JsonNull))
      ) match {
        case Left(error)  => error.field shouldBe "employees"
        case Right(value) => fail(s"Expected error, but was $value")
      }
    }
  }

}
