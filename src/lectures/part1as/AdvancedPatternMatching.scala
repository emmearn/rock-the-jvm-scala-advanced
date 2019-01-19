package lectures.part1as

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description = numbers match {
    case h :: Nil => println(s"the only element is $h")
    case _ => ""
  }

  /*
    - constants
    - wildcars
   */
}
