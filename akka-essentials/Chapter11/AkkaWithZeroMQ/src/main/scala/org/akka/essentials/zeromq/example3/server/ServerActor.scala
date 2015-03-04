package org.akka.essentials.zeromq.example3.server
import akka.zeromq.ZeroMQExtension
import akka.actor.ActorLogging
import akka.actor.Actor
import akka.zeromq.SocketType
import akka.zeromq.ZMQMessage
import akka.zeromq.Bind
import akka.zeromq.Listener

class ServerActor extends Actor with ActorLogging {
  val repSocket = ZeroMQExtension(context.system).newSocket(SocketType.Rep, Bind("tcp://127.0.0.1:1234"), Listener(self))

  def receive: Receive = {
    case m: ZMQMessage =>
      var mesg = m.frame(0)
      repSocket ! ZMQMessage(mesg + " Good to see you!")
  }
}