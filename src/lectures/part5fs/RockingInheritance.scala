package lectures.part5fs

object RockingInheritance extends App {

  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }

  trait GenericStram[T] {
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStram[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  trait Animal { def name: String }
  trait Lion extends Animal { override def name: String = "lion" }
  trait Tiger extends Animal { override def name: String = "tiger" }

  class Mutant extends Lion with Tiger/* {
    override def name: String = "ALIEN"
  } */

  val m = new Mutant
  println(m.name) // LAST OVERRIDE GETS PICKED

  trait Cold {
    def print = println("cold")
  }

  trait Green extends Cold {
    override def print: Unit = {
      println("Green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("Blue")
      super.print
    }
  }

  class Red {
    def print: Unit = {
      println("Red")
    }
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }
  val color = new White
  color.print
}
