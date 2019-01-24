package lectures.part4Implicits

object TypeClasses extends App {
  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/></div>"
  }

  User("John", 32, "john@rockthejvm.com").toHtml

  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) =>
      case _ =>
    }
  }

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/></div>"
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(UserSerializer.serialize(john))
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
  }

  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  // TYPE CLASS
  trait MyTypeTemplate[T] {
    def action(value: T): String
  }

  object MyTypeTemplate {
    def apply[T](implicit instance: MyTypeTemplate[T]) = instance
  }

  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  // access to the entire class interface
  println(HTMLSerializer[User].serialize(john))

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val anotherJohn = User("John", 45, "anotherJohn@rtjvm.com")
  println(Equal(john, anotherJohn))

  // AD-HOC polymorphism
}
