package exercises

import lectures.part4Implicits.TypeClasses.{User}

object EqualityPlayground extends App {
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val john = User("John", 32, "john@rockthejvm.com")
  val anotherJohn = User("John", 45, "anotherJohn@rtjvm.com")
  println(Equal(john, anotherJohn))
  // AD-HOC polymorphism

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(value, other)
  }
  println(john === anotherJohn)
  println(john == 43)
  // println(john === 43) // TYPE SAFE
}
