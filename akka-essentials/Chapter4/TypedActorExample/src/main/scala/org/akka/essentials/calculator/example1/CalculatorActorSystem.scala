package org.akka.essentials.calculator.example1

import org.akka.essentials.calculator._
import akka.actor.ActorSystem
import akka.actor.TypedActor
import akka.actor.TypedProps
import akka.actor.ActorRef
import scala.concurrent.Await
import scala.concurrent.duration._

object CalculatorActorSystem {

    def main(args: Array[String]): Unit = {

        val _system = ActorSystem("TypedActorsExample")

        val calculator: CalculatorInt =
            TypedActor(_system).typedActorOf(TypedProps[Calculator]())

        calculator.incrementCount()

        // Invoke the method and wait for result
        val future = calculator.add(14, 6)
        var result = Await.result(future, Duration(5, SECONDS))
        println("Result is " + result)

        // Invoke the method and wait for result
        var counterResult = calculator.incrementAndReturn()
        println("Result is " + counterResult.get)

        counterResult = calculator.incrementAndReturn()
        println("Result is " + counterResult.get)

        // Get access to the ActorRef
        val calActor: ActorRef = TypedActor(_system).getActorRefFor(calculator)
        // call actor with a message
        calActor.tell("Hi there", ActorRef.noSender)

        _system.shutdown()
    }

}