package org.akka.essentials.future.example

import _root_.java.util.concurrent.TimeUnit
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class ProcessOrderActor extends Actor {

    //import the implicit

    import context._

    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    val orderActor = context.actorOf(Props[OrderActor])
    val addressActor = context.actorOf(Props[AddressActor])
    val orderAggregateActor = context.actorOf(Props[OrderAggregateActor])

    def receive = {
        case userId: Integer =>
            val aggResult: Future[OrderHistory] =
                for {
                    order <- ask(orderActor, userId).mapTo[Order] // call pattern directly
                    address <- addressActor ask userId mapTo manifest[Address] // call by implicit conversion
                } yield OrderHistory(order, address)

            aggResult pipeTo orderAggregateActor
    }
}