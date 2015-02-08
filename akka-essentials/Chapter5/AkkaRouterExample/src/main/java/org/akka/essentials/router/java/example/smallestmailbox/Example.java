package org.akka.essentials.router.java.example.smallestmailbox;

import akka.routing.SmallestMailboxPool;
import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.SmallestMailboxRouter;

public class Example {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ActorSystem _system = ActorSystem.create("SmallestMailBoxRouterExample");
        ActorRef smallestMailBoxRouter = _system.actorOf(Props.create(MsgEchoActor.class)
                .withRouter(new SmallestMailboxPool(5)), "mySmallestMailBoxRouterActor");

        for (int i = 1; i <= 10; i++) {
            //works like roundrobin but tries to rebalance the load based on
            //size of actor's mailbox
            smallestMailBoxRouter.tell(i, ActorRef.noSender());
        }
        _system.shutdown();

    }

}
