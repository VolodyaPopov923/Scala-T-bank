package unmarshal

import data.CompanyEmployee
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import unmarshal.TestSample.{employee1242, employee1242Json, employee1489, employee1489Json}
import unmarshal.encoder.Encoder
import unmarshal.model.Json.{JsonArray, JsonBool, JsonNull, JsonObject, JsonString}
import unmarshal.testclass.Company

class EncoderAutoDeriveSpec extends AnyFlatSpec with Matchers {

  private val company1: Company = Company(
    legalName = "OOO Roga and Kopyta",
    accountNumber = "123456789",
    director = employee1242,
    bookkeeper = None,
    active = true,
    staff = CompanyEmployee(List(employee1489, employee1242))
  )

  private val company2: Company = Company(
    legalName = "OAO A & B",
    accountNumber = "000000000",
    director = employee1489,
    bookkeeper = Some(employee1242),
    active = false,
    staff = CompanyEmployee(List())
  )

  "Encoder.autoDerive[Company]" should "correctly encode Company1" in {
    Encoder.autoDerive[Company].toJson(company1) shouldBe JsonObject(
      Map(
        "legalName"     -> JsonString("OOO Roga and Kopyta"),
        "accountNumber" -> JsonString("123456789"),
        "director"      -> employee1242Json,
        "bookkeeper"    -> JsonNull,
        "active"        -> JsonBool(true),
        "staff" -> JsonObject(
          Map("employees" -> JsonArray(List(employee1489Json, employee1242Json)))
        )
      )
    )
  }

  it should "correctly encode Company2" in {
    Encoder.autoDerive[Company].toJson(company2) shouldBe JsonObject(
      Map(
        "legalName"     -> JsonString("OAO A & B"),
        "accountNumber" -> JsonString("000000000"),
        "director"      -> employee1489Json,
        "bookkeeper"    -> employee1242Json,
        "active"        -> JsonBool(false),
        "staff"         -> JsonObject(Map("employees" -> JsonArray(List())))
      )
    )
  }

}
