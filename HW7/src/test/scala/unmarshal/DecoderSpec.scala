package unmarshal

import data.{CompanyEmployee, Employee}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import unmarshal.decoder.Decoder

class DecoderSpec extends AnyFlatSpec with Matchers with DecoderBehaviors {

  "Decoder[Employee]" should behave like employeeDecoder(Decoder[Employee])

  "Decoder[CompanyEmployee]" should behave like companyEmployeeDecoder(Decoder[CompanyEmployee])

}
