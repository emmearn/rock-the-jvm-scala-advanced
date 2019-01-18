package lectures.part1as

import scala.util.Try

object DarkSugar extends App {
  // #1 mathods with single parameters
  def singleArgumentMethods(x: Int) = println("")

  val description = singleArgumentMethods {
    // write some code
    42
  }

  val aTryInstance = Try { // java try
    throw new RuntimeException
  }

  List(1, 2, 3).map{ x =>
    x + 1
  }

  // #2 single abstract method
  trait Action {
    def act(x: Int): Int
  }

  val anInstance = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1

  // example: Runnable
  val aTread = new Thread(new Runnable {
    override def run(): Unit = println("Hello, Scala")
  })

  val aSweeterThread = new Thread(() => println("Sweet, Scala"))

  abstract class AnAbstractType {
    def implemented: Int = 42
    def f(a: Int): Unit
  }

  val abstractInstance: AnAbstractType = (a: Int) => println("sweet")

  // #3 the :: and #:: are special
  val prependedLIst = 2:: List(3, 4)
  // 2.::(List(3, 4))
  List(3,4).::(2)
  // ?!

  // scala spec: last char decides associavity of method
  1 :: 2 :: 3 :: List(4, 5)
  List(4, 5).::(3).::(2).::(1)

  class MyStreams[T] {
    def -->:(value: T): MyStreams[T] = this
  }

  val myStreams = 1 -->: 2 -->: 3 -->: new MyStreams[Int]

  // #4 multi word methods naming
  class Teengirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }

  val lilly = new Teengirl("Lilly")
  lilly `and then said` "Scala is soo sweet!"

  
}
