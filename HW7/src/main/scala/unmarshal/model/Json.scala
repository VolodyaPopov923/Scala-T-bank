package unmarshal.model

import cats.kernel.Semigroup

sealed trait Json

object Json {

  final case object JsonNull                            extends Json
  final case class JsonString(value: String)            extends Json
  final case class JsonNum(value: Long)                 extends Json
  final case class JsonDouble(value: Double)            extends Json
  final case class JsonBool(value: Boolean)             extends Json
  final case class JsonArray(value: List[Json])         extends Json
  final case class JsonObject(value: Map[String, Json]) extends Json

  implicit val semigroup: Semigroup[JsonObject] = (x: JsonObject, y: JsonObject) =>
    JsonObject(x.value ++ y.value)

}
