package lectures.part4Implicits

// TYPE CLASS
trait MyTypeTemplate[T] {
  def action(value: T): String
}

object MyTypeTemplate {
  def apply[T](implicit instance: MyTypeTemplate[T]) = instance
}