package org.akka.essentials.router.example.scattergatherfirstcomplete

import org.akka.essentials.router.example.RandomTimeActor

import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.ScatterGatherFirstCompletedRouter
import akka.util.Timeout
import scala.concurrent.Await
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

object Example {
    def main(args: Array[String]): Unit = {
        val _system = ActorSystem("SGFCRouterExample")
        val scatterGatherFirstCompletedRouter = _system.actorOf(Props[RandomTimeActor].withRouter(
            ScatterGatherFirstCompletedRouter(nrOfInstances = 5, within = Duration(5, TimeUnit.SECONDS))),
            name = "mySGFCRouterActor")

        implicit val timeout = Timeout(5, TimeUnit.SECONDS)
        val futureResult = scatterGatherFirstCompletedRouter ? "message"
        val result = Await.result(futureResult, timeout.duration)
        System.out.println(result)

        _system.shutdown()
    }
}