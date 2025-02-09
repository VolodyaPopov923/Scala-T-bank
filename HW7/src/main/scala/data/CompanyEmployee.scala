package data

import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder

case class CompanyEmployee(
  employees: List[Employee]
)

object CompanyEmployee {

  implicit def companyEmployeeEncoder: Encoder[CompanyEmployee] = ???
  implicit def companyEmployeeDecoder: Decoder[CompanyEmployee] = ???

}
