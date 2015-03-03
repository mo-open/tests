package org.akka.essentials.remotenode

import akka.actor.{ActorRef, Actor}

class RemoteActor extends Actor {
    def receive: Receive = {
        case message: String =>
            // Get reference to the message sender and reply back
            sender.tell(message + " got something", ActorRef.noSender)
    }
}
