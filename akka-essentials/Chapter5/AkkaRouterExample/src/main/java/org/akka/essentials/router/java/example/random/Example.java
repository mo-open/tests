package org.akka.essentials.router.java.example.random;

import akka.routing.RandomPool;
import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RandomRouter;

public class Example {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ActorSystem _system = ActorSystem.create("RandomRouterExample");
		ActorRef randomRouter = _system.actorOf(Props.create(MsgEchoActor.class)
				.withRouter(new RandomPool(5)),"myRandomRouterActor");

		for (int i = 1; i <= 10; i++) {
			//sends randomly to actors
			randomRouter.tell(i, ActorRef.noSender());
		}
		_system.shutdown();
	}

}
