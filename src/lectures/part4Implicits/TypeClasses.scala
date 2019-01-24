package lectures.part4Implicits

object TypeClasses extends App {
  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml(): String = s"<div>$name ($age yo) <a href=$email/></div>"
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

  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(john.toHTML(UserSerializer))
  println(john.toHTML)

  println(2.toHTML)
  println(john.toHTML(PartialUserSerializer))

  // context bounds
  def htmlBoilerPlate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body>${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T: HTMLSerializer](content: T): String = {
    val serializable = implicitly[HTMLSerializer[T]]
    // use serializer
    s"<html><body>${content.toHTML(serializable)}</body></html>"
  }

  // implicitly
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]
}
