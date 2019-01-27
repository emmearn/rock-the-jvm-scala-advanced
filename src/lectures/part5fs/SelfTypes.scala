package lectures.part5fs

object SelfTypes {

  // requiring a type to be mixed in
  trait InstrumentList {
    def play(): Unit
  }

  trait Singer { self: InstrumentList => // SELF TYPE force whoever implements singer to implement InstrumentList
    def sing(): Unit
  }

  class LeadSinger extends Singer with InstrumentList {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

//  class Vocalist extends Singer {
//    override def sing(): Unit = ???
//  } // illegal

  val jamesHefield = new Singer with InstrumentList {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  class Guitarist extends InstrumentList {
    override def play(): Unit = println("guitar solo")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // vs inheritance
  class A
  class B extends A // B is an A

  trait T
  trait S { self: T => } // S requires a T

  // cake pattern => "dependency injection"

  // Dependency Injection
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  // cake pattern
  trait ScalaComponent {
    // API
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def depedentAction(x: Int): String = action(x) + " this rocks!"
  }
  trait ScalaApplication { self: ScalaDependentComponent => }

  // layer 1 - small component
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics


  // cyclical dependency
//  class X extends Y
//  class Y extends X

  trait X { self: Y => }
  trait Y { self: X => }

}
