package org.akka.essentials.calculator.java.example3;

import org.akka.essentials.calculator.java.CalculatorInt;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class CalculatorActorSytem {

	public static void main(String[] args) throws Exception {

		ActorSystem _system = ActorSystem.create("TypedActorsExample",
				ConfigFactory.load().getConfig("TypedActorExample"));

		CalculatorInt calculator = TypedActor.get(_system).typedActorOf(
				new TypedProps<SupervisorActor>(CalculatorInt.class,
						SupervisorActor.class),"supervisorActor");

		// Get access to the ActorRef
		ActorRef calActor = TypedActor.get(_system).getActorRefFor(calculator);
		// call actor with a message
		calActor.tell("Hi there",calActor);
		
		//wait for child actor to get restarted
		Thread.sleep(500);
		
		// Invoke the method and wait for result
		Timeout timeout = new Timeout(Duration.apply(5, TimeUnit.SECONDS));
	    Future<Object> future = Patterns.ask(calActor, Integer.valueOf(10), timeout);
	    Integer result = (Integer) Await.result(future, timeout.duration());
		
		System.out.println("Result from child actor->" + result);

		//wait before shutting down the system
		Thread.sleep(500);
		
		_system.shutdown();

	}

}
