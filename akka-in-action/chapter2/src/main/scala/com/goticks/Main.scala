package com.goticks

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import akka.io.IO
import spray.can.Http

object Main extends App {
    implicit val system = ActorSystem()
    val config = ConfigFactory.load()
    val host = config.getString("http.host")
    val port = config.getInt("http.port")

    val api = system.actorOf(Props(new RestInterface()), "httpInterface")
    IO(Http) ! Http.Bind(api, interface = host, port = port)
}
