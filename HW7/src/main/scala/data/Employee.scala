package data

import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder

case class Employee(
  name: String,
  age: Long,
  id: Long,
  bossId: Option[Long]
)

object Employee {

  implicit def employeeEncoder: Encoder[Employee] = ???
  implicit def employeeDecoder: Decoder[Employee] = ???

}
