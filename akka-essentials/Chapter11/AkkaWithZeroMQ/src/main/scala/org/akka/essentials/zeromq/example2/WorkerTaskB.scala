package org.akka.essentials.zeromq.example2

import akka.zeromq.ZeroMQExtension
import akka.actor.{ActorRef, ActorLogging, Actor}
import akka.zeromq.ZMQMessage
import akka.zeromq.SocketType
import akka.zeromq.Connect
import akka.zeromq.Listener
import akka.zeromq.Identity

class WorkerTaskB extends Actor with ActorLogging {
    val subSocket = ZeroMQExtension(context.system).newSocket(SocketType.Dealer, Connect("tcp://127.0.0.1:1234"), Listener(self), Identity("B".getBytes()))

    def receive = {
        case m: ZMQMessage =>
            var mesg = m.frame(0)
            subSocket.tell(ZMQMessage(mesg
                + " - Workload Processed by B"), ActorRef.noSender)
    }
}