package org.akka.essentials.dispatcher.example.Dispatcher

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.routing.RoundRobinRouter
import org.akka.essentials.dispatcher.MsgEchoActor

object Example2 {
    def main(args: Array[String]): Unit = {}

    val _system = ActorSystem.create("default-dispatcher", ConfigFactory.load().getConfig("MyDispatcherExample"))

    val actor = _system.actorOf(Props[MsgEchoActor].withDispatcher("defaultDispatcher1").withRouter(
        RoundRobinRouter(5)))

    0 to 25 foreach {
        i => actor ! i
    }
    _system.shutdown()
}