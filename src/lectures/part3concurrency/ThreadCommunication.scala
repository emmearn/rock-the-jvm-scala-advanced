package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {
  /*
    the producer-consumer problem

    producer -> [ x ] -> consumer
   */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int) = value = newValue

    def get = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while(container.isEmpty)
        println("[consumer] actively waiting...")

      println(s"[consumer] I've consumed ${container.get}")
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      var value = 42
      println(s"[producer] I've produced, after long work, the value $value")
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

//  naiveProdCons()

  // wait and notify
  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized {
        container.wait()
      }

      // container must have some value
      println(s"[consumer] I've consumed ${container.get}")
    })

    val producer = new Thread(() => {
      println("[producer] Hard at work...")
      Thread.sleep(2000)
      val value = 42
      container.synchronized {
        println(s"[producer] I'm producing $value")
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

//  smartProdCons()

  /*
    producer -> [ ? ? ?] -> consumer
   */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer] consumer $x")

          // hey consumer, new food for you!
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while(true) {
        buffer.synchronized {
          if(buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer] producing $i")
          buffer.enqueue(i)

          // hey producer, there's empty space available, are you lazy?
          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

//  prodConsLargeBuffer()

  /*
     producer1 -> [ ? ? ? ] -> consumer1
     producer2 ----^     ^---- consumer2
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run() {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          /*
            producer produces value, two consumer are waiting
            notifies ONE consumer
            notifies the other consumer
           */
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue() // OOps.!
          println(s"[consumer $id] consumer $x")

          buffer.notify()
        }

        Thread.sleep(random.nextInt(250))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run() {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer $id] producing $i")
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int) {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }

  multiProdCons(3, 6)
}
