package org.akka.essentials.zeromq.java.example2;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class MyActorSystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("zeromqTest");
		system.actorOf(Props.create(WorkerTaskA.class), "workerA");
		system.actorOf(Props.create(WorkerTaskB.class), "workerB");
		system.actorOf(Props.create(RouterActor.class), "router");
	}

}
