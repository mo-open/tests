package org.akka.essentials.router.java.example.roundrobin;

import akka.routing.RoundRobinPool;
import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

import java.util.concurrent.TimeUnit;

public class Example {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ActorSystem _system = ActorSystem.create("RoundRobinRouterExample");
        ActorRef roundRobinRouter = _system.actorOf(Props.create(
                MsgEchoActor.class).withRouter(new RoundRobinPool(5)), "myRoundRobinRouterActor");

        for (int i = 1; i <= 10; i++) {
            //sends messages in a round robin way to all the actors
            roundRobinRouter.tell(i, ActorRef.noSender());
            if (i == 5) {
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println("\n");
            }
        }
        _system.shutdown();
    }

}
