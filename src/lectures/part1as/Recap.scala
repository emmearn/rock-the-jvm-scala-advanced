package lectures.part1as

import scala.annotation.tailrec
import scala.util.Try

object Recap extends App {
  val aCondition: Boolean = false
  val aConditionedVal = if(aCondition) 42 else 65
  // instructions vs expressions

  // compiler infers types for us
  val aCodeblock = {
    if(aCondition) 54
    56
  }

  // Unit = void
  val theUnit = println("Hello, Scala")

  // functions
  def aFunctions(x: Int): Int = x + 1

  // recursion: stack and tail
  @tailrec
  def factorial(n: Int, accumulator: Int): Int =
    if(n < 0) accumulator
    else factorial(n - 1, n * accumulator)

  // OOP
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Croccodile extends Animal with Carnivore {
    override def eat(a: Animal) = println("Crunch")
  }

  // methods notations
  val aCroc = new Croccodile
  aCroc eat aDog // natural language

  1 + 2
  1.+(2)

  // Anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar")
  }

  // generics
  abstract class MyList[+A] // variance and variance problems in THIS course

  // singletons object and companions
  object MyList

  // case classes
  case class Person(name: String, age: Int)

  // exceptions, try catch finally expression
  val throwsException = throw new RuntimeException // nothing
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: RuntimeException => "I caught an exception"
  } finally {
    println("some logs")
  }

  // packaging and imports

  // functional programming
  val incrementer = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1

  List(1, 2, 3).map(x => anonymousIncrementer(x)) // HOF
  // map, flatmap, filter

  // for-comprehension
  val pairs = for {
    num <- List(1, 2, 3) // filter
    char <- List('a', 'b', 'c')
  } yield num + " " + char

  // scala collections
  // sequence arrays lists vectors maps tuples
  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // "collections": Options, Try
  val anOption = Some(3)

  // pattern matching
  val x = 2
  val aMatch = x match {
    case 1 => "it's first"
    case 2 =>
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi my name is $n"
  }

  // all the patterns
}
