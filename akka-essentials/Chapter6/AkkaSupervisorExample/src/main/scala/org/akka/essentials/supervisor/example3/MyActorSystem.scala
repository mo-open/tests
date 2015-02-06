package org.akka.essentials.supervisor.example3
import akka.actor.ActorSystem
import akka.actor.Props

import akka.actor.ActorRef

case class Result()
case class DeadWorker()
case class RegisterWorker(val worker: ActorRef, val supervisor: ActorRef)

object MyActorSystem extends App {

  val system = ActorSystem("faultTolerance")
  val log = system.log

  val supervisor = system.actorOf(Props[SupervisorActor], name = "supervisor")

  var mesg: Int = 8;
  supervisor ! mesg

  supervisor ! "Do Something"

  Thread.sleep(4000)

  supervisor ! mesg

  system.shutdown();

}