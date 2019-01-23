package exercises

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success, Try}

object FuturesAndPromisesExercises extends App {

  def fullFillImmediately[T](value: T): Future[T] = Future(value)

  def inSequence[A,B](first: Future[A], second: Future[B]): Future[B] =
    first.flatMap(_ => second)

  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]

//    def tryComplete(promise: Promise[A], result: Try[A]) = result match {
//      case Success(r) => try {
//        promise.success(r)
//      } catch {
//        case _ =>
//      }
//      case Failure(t) => try {
//        promise.failure(t)
//      } catch {
//        case _ =>
//      }
//    }

    fa.onComplete(promise.tryComplete) // tryComplete(promise, _)
    fb.onComplete(promise.tryComplete)

    promise.future
  }

  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]

    val checkAndComplete = (result: Try[A]) =>
      if(!bothPromise.tryComplete(result))
        lastPromise.complete(result)

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)

    lastPromise.future
  }

  val fast = Future {
    Thread.sleep(100)
    42
  }

  val slow = Future {
    Thread.sleep(200)
    45
  }

  first(fast, slow).foreach(f => println(s"FIRST: $f")) // 42
  last(fast, slow).foreach(l => println(s"LAST: $l")) // 45

  Thread.sleep(1000)

  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action()
    .filter(condition)
    .recoverWith {
      case _ => retryUntil(action, condition)
    }

  val random = new Random()

  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println(s"generated $nextValue")
    nextValue
  }

  retryUntil(action, (x: Int) => x < 10).foreach(result => println(s"settled at $result"))

  Thread.sleep(10000)
}
