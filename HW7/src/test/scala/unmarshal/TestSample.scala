package unmarshal

import data.Employee
import unmarshal.model.Json.{JsonNull, JsonNum, JsonObject, JsonString}

object TestSample {

  val employee1242: Employee = Employee("Jon", 42, 1242, None)
  val employee1489: Employee = Employee("Bony", 35, 1489, Some(7))

  val employee1242Json: JsonObject = JsonObject(
    Map(
      "name"   -> JsonString("Jon"),
      "age"    -> JsonNum(42),
      "id"     -> JsonNum(1242),
      "bossId" -> JsonNull
    )
  )

  val employee1489Json: JsonObject = JsonObject(
    Map(
      "name"   -> JsonString("Bony"),
      "age"    -> JsonNum(35),
      "id"     -> JsonNum(1489),
      "bossId" -> JsonNum(7)
    )
  )

}
