package lectures.part4Implicits

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  // method overloading

  class P2PRequest
  class P2PReponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PReponse): Int
    def receive[T: Serializer](message: T): Int // (implicit serializer: Serializer[T])
    def receive[T: Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    // lots of overloads
  }

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PReponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("Handling P2P response")
      24
    }
  }

  receive(new P2PRequest)
  receive(new P2PReponse)

  implicit class FromResponseFuture(future: Future[P2PReponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }

  println(receive(Future(new P2PRequest)))
  println(receive(Future(new P2PReponse)))

  trait MathLib {
    def add1(x: Int) = x + 1
    def add1(s: String) = s.toInt + 1
    // add1 overloads
  }

  // "magnetize"
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _

  println(addFV(1))
  println(addFV("3"))

  val receiveFV = receive _
  // receiveFV(new FromP2PResponse)

  class Handler {
    def handle(s: => String) = {
      println(s)
      println(s)
    }
  }

  trait HandlerMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandlerMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandlerMagnet {
    def apply = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala")
    "ahahah"
  }

//  handle(sideEffectMethod())
  handle {
    println("Hello, Scala")
    "ahahah"
  }
}
