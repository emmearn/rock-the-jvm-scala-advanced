package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  def apply(elem: A): Boolean =
    contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A => B): MySet[B]
  def flatmap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
}

class EmptySet[A] extends MySet[A] {
  def contains(elem: A): Boolean =
    false

  def +(elem: A): MySet[A] =
    new NonEmptySet[A](elem, this)

  def ++(anotherSet: MySet[A]): MySet[A] =
    anotherSet

  def map[B](f: A => B): MySet[B] =
    new EmptySet[B]

  def flatmap[B](f: A => MySet[B]): MySet[B] =
    new EmptySet[B]

  def filter(predicate: A => Boolean): MySet[A] = this

  def foreach(f: A => Unit): Unit =
    ()
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean =
    elem == head || tail.contains(elem)

  def +(elem: A): MySet[A] =
    if(this contains elem) this
    else new NonEmptySet[A](elem, this)

  /*
    [1, 2, 3] ++ [4, 5] =
    [2, 3] ++ [4, 5] + 1 =
    [3] ++ [4 5] + 1 + 2 =
    [] ++ [4 5] + 1 + 2 + 3 =
    [4 5] + 1 + 2 + 3 = [4, 5, 1, 2, 3]
   */
  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head // tail.++(anotherSet + head)

  def map[B](f: A => B): MySet[B] =
    (tail map f) + f(head)

  def flatmap[B](f: A => MySet[B]): MySet[B] =
    (tail flatmap f) ++ f(head)

  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate

    if(predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }
}

object MySet {
  /*
    val s = MySet(1, 2, 3) = buildSet(seq(1, 2, 3), []) =
    builSet(seq(2, 3), [] + 1) =
    builSet(seq(3), [1] + 2) =
    builSet(seq(), [1, 2] + 3) =
    [1, 2, 3]
   */
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if(valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3, 4)
  s + 5 ++ MySet(-1, -2) + 3 flatmap (x => MySet(x, x * 10)) filter(_ % 2 == 0) foreach println
}