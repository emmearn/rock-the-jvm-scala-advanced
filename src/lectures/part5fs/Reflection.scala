package lectures.part5fs

object Reflection extends App {

  // reflection + macros + quasiquotes => METAPROGRAMMING

  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi, my name is $name")
  }

  import scala.reflect.runtime.{universe => ru}

  val m = ru.runtimeMirror(getClass.getClassLoader)
  val clazz = m.staticClass("lectures.part5fs.Reflection.Person") // creating a class object by name
  val cm = m.reflectClass(clazz)
  val constructor = clazz.primaryConstructor.asMethod
  val constructorMirror = cm.reflectConstructor(constructor)
  val instance = constructorMirror.apply("John")

  println(instance)

  val p = Person("Mary") // from the wire as a serialized object
  val methodName = "sayMyName"

  val reflected = m.reflect(p)
  val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod

  val method = reflected.reflectMethod(methodSymbol)
  method.apply()

  // type erasure

  // pp #1 differentiate types at runtime
  val numbers = List(1, 2, 3)

  numbers match {
    case listOfStrings: List[String] => println("list of strigns")
    case listOfNumbers: List[Int] => println("list of numbers")
  }

  // pp #2 limitations on overloads
  def processList(list: List[Int]): Int = 42
  // def processList(list: List[String]): Int = 45

  // TypeTags
  import ru._

  val ttag = typeTag[Person]
  println(ttag.tpe)

  class MyMap[K, V]
  def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]) = typeTag.tpe match {
    case TypeRef(_, _, typeArguments) => typeArguments
    case _ => List()
  }

  val myMap = new MyMap[Int, String]
  val typeArgs = getTypeArguments(myMap) // (typeTag: TypeTag[MyMap[Int, String]])
  println(typeArgs)

  def isSubtype[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
    ttagA.tpe <:< ttagB.tpe
  }

  class Animal
  class Dog extends Animal
  println(isSubtype[Dog, Animal])

  val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod

  val sameMethod = reflected.reflectMethod(anotherMethodSymbol)
  sameMethod.apply()
}
