package org.kafecho.learning.jozispray

import akka.io.IO
import spray.can.Http
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import spray.http.HttpResponse
import spray.http.HttpRequest

/** This example illustrates how a Spray Can Http Server works at the lowest level.
  * It is simply a flow of messages handled by actors.
  */
object HelloWorldHttpServer extends App {

  class HttpHandler extends Actor with ActorLogging {
  
    def receive: Receive = {
      case m: Http.Connected =>
        log.info("Received:{}.", m)
        // This actor will handle all Http request itself (singleton pattern).
        sender ! Http.Register(self)

      case h: HttpRequest =>
        log.info("Got a request:{}.", h)
        sender ! HttpResponse().withEntity("Hello World\n")
    }
  }

  implicit val system = ActorSystem()
  val httpHandler = system.actorOf(Props[HttpHandler])
 
  IO(Http) ! Http.Bind(httpHandler, interface = "localhost", port = 8080)
}