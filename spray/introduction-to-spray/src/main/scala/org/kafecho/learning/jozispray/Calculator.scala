package org.kafecho.learning.jozispray

import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem

object Calculator extends SimpleRoutingApp with App {
  implicit val system = ActorSystem("jozispray")

  startServer("localhost", 8080) {
    pathPrefix("api") {
      pathPrefix("calculator") {
        path("plus" / IntNumber / IntNumber) { (a, b) =>
          get {
            complete("Answer: " + (a + b))
          }
        } ~
          path("minus" / IntNumber / IntNumber) { (a, b) =>
            get {
              complete("Answer: " + (a - b))
            }
          } ~
          path("multiply" / IntNumber / IntNumber) { (a, b) =>
            get {
              complete("Answer: " + (a * b))
            }
          } ~
          path("divide" / IntNumber / IntNumber) { (a, b) =>
            get {
              complete("Answer: " + (a / b))
            }
          }
      }
    }
  }
}