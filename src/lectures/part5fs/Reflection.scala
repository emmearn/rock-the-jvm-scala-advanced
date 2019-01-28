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
}
