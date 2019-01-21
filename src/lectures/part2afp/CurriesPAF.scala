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

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c

  val inserName = concatenator("Hello, I'm ", _ : String, ", how are you?")
  println(inserName("Marco"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _:String)
  println(fillInTheBlanks("Marco", " Scala is awesome!"))

  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(simpleFormat))
  println(numbers.map(preciseFormat))

  println(numbers.map(curriedFormatter("%8.6f"))) // compiler does sweet eta-expansions for us

  def byName(n: Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42
}
