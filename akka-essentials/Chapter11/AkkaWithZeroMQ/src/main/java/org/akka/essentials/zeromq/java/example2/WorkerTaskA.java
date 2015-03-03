package org.akka.essentials.zeromq.java.example2;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.util.ByteString;
import akka.zeromq.Connect;
import akka.zeromq.Identity;
import akka.zeromq.Listener;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

public class WorkerTaskA extends UntypedActor {
	ActorRef subSocket = ZeroMQExtension.get(getContext().system())
			.newDealerSocket(
					new SocketOption[] { new Connect("tcp://127.0.0.1:1237"),
							new Listener(getSelf()),
							new Identity("A".getBytes()) });
	
	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof ZMQMessage) {
			ZMQMessage m = (ZMQMessage) message;
            String mesg = m.frame(0).utf8String();
			subSocket.tell((ZMQMessage.withFrames(ByteString.fromString(mesg
                    + " Processed the workload for A"))),getSelf());
		}

	}

}
