package lectures.part2afp

object PartialFunctions extends App {
  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int
  val aFussyFunction = (x: Int) =>
      if(x == 1) 42
      else if(x == 2) 56
      else if(x == 5) 999
      else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 3 => 999
  }
  // {1, 2, 5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 3 => 999
  } // partial function value

  println(aPartialFunction(2))
  // println(aPartialFunction(3463))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(98))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }
}
