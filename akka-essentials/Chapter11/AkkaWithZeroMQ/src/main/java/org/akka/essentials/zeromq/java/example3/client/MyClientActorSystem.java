package org.akka.essentials.zeromq.java.example3.client;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class MyClientActorSystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("zeromqClientTest");
		system.actorOf(Props.create(ClientActor.class)
				.withRouter(new RoundRobinPool(3)), "client");
	}

}
