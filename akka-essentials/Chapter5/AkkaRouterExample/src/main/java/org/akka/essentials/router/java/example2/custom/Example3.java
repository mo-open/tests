package org.akka.essentials.router.java.example2.custom;

import akka.routing.RandomGroup;
import akka.routing.RandomPool;
import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.DefaultResizer;
import akka.routing.RandomRouter;

public class Example3 {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ActorSystem _system = ActorSystem.create("CustomRouteeRouterExample");

        int lowerBound = 2;
        int upperBound = 15;
        DefaultResizer resizer = new DefaultResizer(lowerBound, upperBound);

        ActorRef randomRouter = _system.actorOf(Props.create(MsgEchoActor.class)
                .withRouter(new RandomPool(2).withResizer(resizer)));

        for (int i = 1; i <= 10; i++) {
            // sends randomly to actors
            randomRouter.tell(i, ActorRef.noSender());
        }
        _system.shutdown();
    }

}
