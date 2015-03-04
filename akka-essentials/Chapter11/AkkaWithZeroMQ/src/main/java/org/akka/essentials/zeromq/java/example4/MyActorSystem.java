package org.akka.essentials.zeromq.java.example4;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class MyActorSystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("zeromqTest");
		system.actorOf(Props.create(PushActor.class), "push");
		system.actorOf(Props.create(PullActor1.class), "pull1");
		system.actorOf(Props.create(PullActor2.class), "pull2");
	}

}
