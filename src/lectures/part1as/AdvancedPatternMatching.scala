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
   - case classes
   - tuples
   - like special magi like above
   */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] ={
      if(person.age < 21) None
      else Some(person.name, person.age)
    }

    def unapply(age: Int): Option[(String)] =
      Some(if(age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a yo."
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)

  object event {
    def unapply(arg: Int): Boolean =
      arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Boolean =
      arg > -10 && arg < 10
  }

  val n: Int = 8
  val mathProperty = n match {
    case singleDigit() => "single digit"
    case event() => "an even number"
    case _ => "no property"
  }

  println(mathProperty)


}
