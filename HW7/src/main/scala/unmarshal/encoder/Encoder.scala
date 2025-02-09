package unmarshal.encoder

import unmarshal.model.Json

trait Encoder[A] {
  def toJson(value: A): Json
}

object Encoder {

  def apply[A](implicit
    encoder: Encoder[A]
  ): Encoder[A] = encoder

  def autoDerive[A]: Encoder[A] = ???

}
