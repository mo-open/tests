package org.akka.essentials.router.custom.example

import akka.actor.{ActorSystem, SupervisorStrategy, Props}
import akka.routing._
import akka.routing.Router
import scala.collection.immutable
import akka.dispatch.Dispatchers

class RedundancyRoutingLogic(nbrCopies: Int) extends RoutingLogic {
    val roundRobin = RoundRobinRoutingLogic()

    def select(message: Any, routees: immutable.IndexedSeq[Routee]): Routee = {
        val targets = (1 to nbrCopies).map(_ => roundRobin.select(message, routees))
        SeveralRoutees(targets)
    }
}

class BurstyMessageRouter(noOfInstances: Int, messageBurst: Int) extends Group {
    var messageCount = 0
    var actorSeq = 0

    def paths = List("/user/s1", "/user/s2", "/user/s3")

    override val routerDispatcher: String = Dispatchers.DefaultDispatcherId

    def createRouter(system: ActorSystem): Router = {
        new Router(new RedundancyRoutingLogic(noOfInstances))
    }
}