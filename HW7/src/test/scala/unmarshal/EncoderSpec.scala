package unmarshal

import data.{CompanyEmployee, Employee}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import unmarshal.encoder.Encoder

class EncoderSpec extends AnyFlatSpec with Matchers with EncoderBehaviors {

  "Encoder[Employee]" should behave like employeeEncoder(Encoder[Employee])

  "Encoder[CompanyEmployee]" should behave like companyEmployeeEncoder(Encoder[CompanyEmployee])

  "Encoder.autoDerive[Employee]" should behave like employeeEncoder(Encoder.autoDerive[Employee])

  "Encoder.autoDerive[CompanyEmployee]" should behave like companyEmployeeEncoder(
    Encoder.autoDerive[CompanyEmployee]
  )

}
