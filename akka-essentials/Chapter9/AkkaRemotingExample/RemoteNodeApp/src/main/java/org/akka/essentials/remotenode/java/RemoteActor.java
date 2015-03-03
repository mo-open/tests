package org.akka.essentials.remotenode.java;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class RemoteActor extends UntypedActor {
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			// Get reference to the message sender and reply back
			getSender().tell(message + " got something", ActorRef.noSender());
		}
	}
}
