package lectures.part2afp

object LazyEvaluation extends App {
  // lazy DELAYS the evaluation of values
  lazy val x: Int = throw new RuntimeException
  // println(x)

  lazy val y: Int = {
    println("Hello")
    42
  }
  println(y)
  println(y)

  // example of implications:
  // side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if(simpleCondition && lazyCondition) "yes" else "no")

  // in conjunction with call by name
  def byNameMethod(n: => Int): Int = n + n + n + 1
  def retrieveMagicValue = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))

  // use lazy vals
  def byNameMethodSmart(n: => Int): Int = {
    lazy val t = n // only evaluated once
    t + t + t + 1
  }

  println(byNameMethodSmart(retrieveMagicValue)) // use lazy vals

  // filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 35, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThan30)
  val gt20Lazy = lt30lazy.withFilter(greaterThan20)
  println
  gt20Lazy.foreach(println)

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy vals
  } yield a + 1

  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatmap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of this stream
    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }
}
