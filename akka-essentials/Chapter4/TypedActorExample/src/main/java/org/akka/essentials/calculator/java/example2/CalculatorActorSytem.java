package org.akka.essentials.calculator.java.example2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import akka.routing.BroadcastGroup;
import akka.routing.BroadcastPool;
import org.akka.essentials.calculator.java.Calculator;
import org.akka.essentials.calculator.java.CalculatorInt;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.TypedActor;
import akka.actor.TypedProps;

public class CalculatorActorSytem {

    public static void main(String[] args) throws Exception {
        ActorSystem _system = ActorSystem.create("TypedActorsExample");

        CalculatorInt calculator1 = TypedActor.get(_system).typedActorOf(
                new TypedProps<Calculator>(CalculatorInt.class,
                        Calculator.class));

        CalculatorInt calculator2 = TypedActor.get(_system).typedActorOf(
                new TypedProps<Calculator>(CalculatorInt.class,
                        Calculator.class));

        // Create a router with Typed Actors
        ActorRef actor1 = TypedActor.get(_system).getActorRefFor(calculator1);
        ActorRef actor2 = TypedActor.get(_system).getActorRefFor(calculator2);

        Iterable<ActorRef> routees = Arrays.asList(new ActorRef[]{actor1,
                actor2});
        List<String> routeePaths = new ArrayList<String>();
        routeePaths.add(TypedActor.get(_system).getActorRefFor(actor1).path().toStringWithoutAddress());
        routeePaths.add(TypedActor.get(_system).getActorRefFor(actor2).path().toStringWithoutAddress());

        ActorRef router = _system.actorOf(new BroadcastGroup(routeePaths).props());

        router.tell("Hello there", ActorRef.noSender());

        _system.shutdown();

    }

}
