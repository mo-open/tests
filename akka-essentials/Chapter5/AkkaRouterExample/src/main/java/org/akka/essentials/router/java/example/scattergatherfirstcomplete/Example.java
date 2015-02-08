package org.akka.essentials.router.java.example.scattergatherfirstcomplete;

import akka.routing.ScatterGatherFirstCompletedPool;
import org.akka.essentials.router.example.RandomTimeActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Example {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem
				.create("ScatterGatherFirstCompletedRouterExample");
		ActorRef scatterGatherFirstCompletedRouter = _system.actorOf(Props.create(
                RandomTimeActor.class)
				.withRouter(new ScatterGatherFirstCompletedPool(5, Duration
						.apply(5, TimeUnit.SECONDS))),"myScatterGatherFirstCompletedRouterActor");

		Timeout timeout = new Timeout(Duration.apply(10,TimeUnit.SECONDS));
		Future<Object> futureResult = akka.pattern.Patterns.ask(
				scatterGatherFirstCompletedRouter, "message", timeout);
		String result = (String) Await.result(futureResult, timeout.duration());
		System.out.println(result);
		
		_system.shutdown();

	}

}
