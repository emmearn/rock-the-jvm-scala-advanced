package lectures.part2afp

object CurriesPAF extends App {
  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(superAdder(3)(5)) // curried function

  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)

  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1

  List(1, 2, 3).map(inc) // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  val add7 = (x: Int) => simpleAddFunction(7, x)
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _ : Int) // works well

  val add7_3 = curriedAddMethod(7) _ // Partially Apply Function
  val add7_4 = curriedAddMethod(7)(_) // alternative syntax
  val add7_5 = simpleAddMethod(7, _ : Int) // alternative syntax 
}
