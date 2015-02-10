package org.kafecho.learning.jozispray

import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object MeaningOfLife {
  case object WhatIsTheMeaningOfLife
  case class Answer(number: Int)
}

// An Actor which provides the meaning of life when prompted.
class MeaningOfLife extends Actor {
  import MeaningOfLife._
  def receive = {
    case WhatIsTheMeaningOfLife =>
      sender ! Answer(42)
  }
}

object HelloActor extends SimpleRoutingApp with App {
  import MeaningOfLife._
  implicit val system = ActorSystem("restafarian")
  import system.dispatcher
  val actor = system.actorOf(Props[MeaningOfLife])

  // How long to wait for the actor's reply.
  implicit val askTimeout = Timeout(2 seconds)

  startServer(interface = "localhost", port = 8080) {
    pathPrefix("api") {
      path("hello-actor") {
        get {
          complete {
            val future = (actor ? WhatIsTheMeaningOfLife).mapTo[Answer]
            future.map(a => s"The meaning of life is: ${a.number}.")
          }
        }
      }
    }
  }
}
