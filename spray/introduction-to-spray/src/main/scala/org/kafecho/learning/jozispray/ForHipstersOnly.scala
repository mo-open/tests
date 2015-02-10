package org.kafecho.learning.jozispray

import spray.routing.SimpleRoutingApp
import spray.routing.Directive
import shapeless.HNil
import spray.routing.MissingHeaderRejection
import spray.routing.MalformedHeaderRejection
import akka.actor.ActorSystem

object ForHipstersOnly extends SimpleRoutingApp with App {
  implicit val system = ActorSystem("test")

  val hipsterHeader = "X-Hipster"

  /** A directive that checks that the Hipster header is present and set to true. If not the request is rejected.*/
  val forHipstersOnly: Directive[HNil] = optionalHeaderValueByName(hipsterHeader).flatMap { header =>
    header match {
      case Some("true") => pass
      case Some(_) => reject(MalformedHeaderRejection(hipsterHeader, s"Sorry, you are not a hipster", None))
      case None => reject(MissingHeaderRejection(hipsterHeader))
    }
  }

  val route = pathPrefix("api") {
    path("hipster-tips") {
      complete("To increase your hipster profile, you should move to Cape Town!")
    }
  }

  startServer("localhost", 8080) {
    forHipstersOnly {
      route
    }
  }
}