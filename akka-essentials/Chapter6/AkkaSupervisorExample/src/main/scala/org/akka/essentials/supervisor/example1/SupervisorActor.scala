package org.akka.essentials.supervisor.example1

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props

import scala.concurrent.duration._
import scala.language.postfixOps

class SupervisorActor extends Actor with ActorLogging {

    import akka.actor.OneForOneStrategy
    import akka.actor.SupervisorStrategy._

    val childActor = context.actorOf(Props[WorkerActor], name = "workerActor")

    override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10,
        withinTimeRange = Duration(10, SECONDS)) {

        case _: ArithmeticException => Resume
        case _: NullPointerException => Restart
        case _: IllegalArgumentException => Stop
        case _: Exception => Escalate
    }

    def receive = {
        case result: Result =>
            childActor.tell(result, sender)
        case msg: Object =>
            childActor ! msg

    }
}