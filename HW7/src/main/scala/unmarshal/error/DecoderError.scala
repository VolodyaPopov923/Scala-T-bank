package unmarshal.error

final case class DecoderError protected (
  message: String,
  field: String
)

object DecoderError {

  def wrongJson(reason: String, field: String): DecoderError =
    new DecoderError(
      message = s"Illegal json at '$field': $reason",
      field = field
    )

}
