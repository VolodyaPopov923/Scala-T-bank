package unmarshal

import data.{CompanyEmployee, Employee}
import org.scalacheck.Gen.choose
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Properties}
import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder

object UnmarshalPropSpec extends Properties("Decoder & Encoder") {

  property("law x = decode(encode(x)) for Employee") = forAll[Employee, Boolean](law[Employee](_))

  property("law x = decode(encode(x)) for CompanyEmployee") =
    forAll[CompanyEmployee, Boolean](law[CompanyEmployee](_))

  property("law x = decode(encode(x)) for Employee (autoDerive)") = forAll { employee: Employee =>
    law[Employee](employee)(Decoder[Employee], Encoder.autoDerive[Employee])
  }

  property("law x = decode(encode(x)) for CompanyEmployee (autoDerive)") = forAll {
    companyEmployee: CompanyEmployee =>
      law[CompanyEmployee](companyEmployee)(
        Decoder[CompanyEmployee],
        Encoder.autoDerive[CompanyEmployee]
      )
  }

  private def law[T](value: T)(implicit
    decoder: Decoder[T],
    encoder: Encoder[T]
  ): Boolean =
    decoder.fromJson(encoder.toJson(value)) == Right(value)

  implicit val employeeArb: Arbitrary[Employee] = Arbitrary {
    for {
      name   <- Gen.alphaLowerStr
      age    <- Gen.choose(18L, 85L)
      id     <- Gen.long
      bossId <- Gen.option(Gen.long)
    } yield Employee(name, age, id, bossId)
  }

  implicit val companyEmployeeArb: Arbitrary[CompanyEmployee] = Arbitrary {
    choose(0, 12)
      .flatMap(size => Gen.listOfN(size, employeeArb.arbitrary))
      .map(CompanyEmployee(_))
  }

}
