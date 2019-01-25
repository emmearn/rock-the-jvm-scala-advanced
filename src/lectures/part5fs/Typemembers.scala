package lectures.part5fs

object Typemembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???

//  val cat: ac.BoundedAnimal = new Cat

  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // alternative to generics
  trait MyList/*[T]*/ {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int
    def add(element: Int): MyList = ???
  }
  // .type
  type CatsType = cat.type
  val newCat: CatsType = cat
  // new CatsType // doesn't works

  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumber {
    type A <: Number
  }
  // NOT OK
//  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumber {
//    override type A = String
//    def head = hd
//    def tail = tl
//  }

  // OK
  class IntList(hd: Int, tl: IntList) extends MList {
    override type A = Int
    def head = hd
    def tail = tl
  }
}
