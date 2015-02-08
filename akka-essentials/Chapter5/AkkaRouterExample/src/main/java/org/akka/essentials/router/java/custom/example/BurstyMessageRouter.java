package org.akka.essentials.router.java.custom.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.dispatch.Dispatchers;
import akka.routing.*;
import scala.collection.immutable.IndexedSeq;

/**
 * Router that sends burst of packets to one actor before sending
 * to the next. The packet burst is a configurable value
 *
 * @author Munish
 */
class RedundancyRoutingLogic implements RoutingLogic {
    RoundRobinRoutingLogic roundRobin = new RoundRobinRoutingLogic();
    private int nCopies;

    public RedundancyRoutingLogic(int nCopies) {
        this.nCopies = nCopies;
    }

    @Override
    public Routee select(Object message, IndexedSeq<Routee> routees) {
        List<Routee> targets = new ArrayList();
        for (int i = 0; i < this.nCopies; i++) {
            targets.add(roundRobin.select(message, routees));
        }
        roundRobin.select(message, routees);
        return new SeveralRoutees(targets);
    }
}

public class BurstyMessageRouter extends CustomRouterConfig {

    int noOfInstances;
    int messageBurst;

    public BurstyMessageRouter(int inNoOfInstances, int inMessageBurst) {
        noOfInstances = inNoOfInstances;
        messageBurst = inMessageBurst;
    }

    @Override
    public Router createRouter(ActorSystem system) {
        return new Router(new RedundancyRoutingLogic(noOfInstances));
    }

    public String routerDispatcher() {
        return Dispatchers.DefaultDispatcherId();
    }

    public SupervisorStrategy supervisorStrategy() {
        return SupervisorStrategy.defaultStrategy();
    }
}
