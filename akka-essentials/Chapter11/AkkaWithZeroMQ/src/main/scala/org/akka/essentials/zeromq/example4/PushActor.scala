package org.akka.essentials.zeromq.example4
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Cancellable
import akka.zeromq.Bind
import akka.zeromq.Listener
import akka.zeromq.SocketType
import akka.zeromq.ZMQMessage
import akka.zeromq.ZeroMQExtension
import scala.concurrent.duration._

case class Tick()

class PushActor extends Actor with ActorLogging {
	val pushSocket = ZeroMQExtension(context.system).newSocket(SocketType.Push, Bind("tcp://127.0.0.1:1234"), Listener(self))

	var count = 0
	var cancellable: Cancellable = null
	override def preStart() {
		cancellable = context.system.scheduler.schedule(1 second, 1 second, self, Tick)
	}

	def receive: Receive = {
		case Tick =>
			count += 1
			val payload = "Hi there! (" + count + ")"
			pushSocket ! ZMQMessage(payload))
			if (count == 5) {
				cancellable.cancel()
			}
	}
}