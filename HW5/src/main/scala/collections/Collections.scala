package collections

object Collections {

  /*
    In a sorted list find all pairs of two neighbor numbers which have a gap between them
    None for Seq(1, 2, 3, 4)
    Some(Seq((2, 8))) for Seq(1, 2, 8)
    Some(Seq((3, 5), (5, 7))) for Seq(3, 5, 7)
   */
  def findGaps(l: Seq[Int]): Option[Seq[(Int, Int)]] = {
    ???
  }

  /*
    Find key-value pair with the minimum value in the map
    try to implement min in different ways (fold, reduce, recursion)
   */
  def minFold(map: Map[String, Int]): Option[(String, Int)] = {
    ???
  }

  def minReduce(map: Map[String, Int]): Option[(String, Int)] = {
    ???
  }

  def minRecursion(map: Map[String, Int]): Option[(String, Int)] = ???

  // Implement scanLeft - running total, applying [f] to elements of [list] (not using scans ofc)
  def scanLeft[T](zero: T)(list: Seq[T])(f: (T, T) => T): Seq[T] = {
    ???
  }

  // Count the consecutive occurrences of each character in the string
  def count(s: String): List[(Char, Int)] = {
    ???
  }
}
