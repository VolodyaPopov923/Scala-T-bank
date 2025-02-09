package unmarshal.decoder

import unmarshal.error.DecoderError
import unmarshal.model.Json

trait Decoder[A] {
  def fromJson(json: Json): Either[DecoderError, A]
}

object Decoder {

  implicit def apply[A](implicit
    decoder: Decoder[A]
  ): Decoder[A] = decoder

}
