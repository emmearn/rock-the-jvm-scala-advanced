package lectures.part5fs

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object HigherKindedTypes extends App {

  trait AHigherKindedTypes[F[_]]

  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
  }

  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
  }

  trait MyFuture[T] {
    def flatMap[B](f: T => B): MyFuture[B]
  }

  // combine/multiply List(1, 2) x List("a", "b") => List(1a, 1b, 2a, 2b
//  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
//    for {
//      a <- listA
//      b <- listB
//    } yield (a, b)
//
//  def multiply[A, B](listA: Option[A], listB: Option[B]): Option[(A, B)] =
//    for {
//      a <- listA
//      b <- listB
//    } yield (a, b)
//
//  def multiply[A, B](listA: Future[A], listB: Future[B]): Future[(A, B)] =
//    for {
//      a <- listA
//      b <- listB
//    } yield (a, b)

  // use Higher kinded type
  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  implicit class MonadsList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadsOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  def multiply[F[_], A, B](implicit ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  val monadsList = new MonadsList(List(1, 2, 3))
  monadsList.flatMap(x => List(x, x + 1)) // List[Int]
  monadsList.map(_ * 2)

  println(multiply(new MonadsList(List(1, 2)), new MonadsList(List("a", "b"))))
  println(multiply(new MonadsOption[Int](Some(2)), new MonadsOption[String](Some("scala"))))

  println(multiply(List(1, 2), List("a", "b")))
  println(multiply(Some(2), Some("scala")))
}
