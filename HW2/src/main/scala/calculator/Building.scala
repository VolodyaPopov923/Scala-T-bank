package calculator

sealed trait Building(length: Int, width: Int, height: Int, floorNumber: Int)

object Building {

  final case class Economy(length: Int, width: Int, height: Int, floorNumber: Int)
      extends Building(length, width, height, floorNumber)

  final case class Premium(length: Int, width: Int, height: Int, floorNumber: Int)
      extends Building(length, width, height, floorNumber)

}
