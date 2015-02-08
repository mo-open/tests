package org.akka.essentials.router.java.example.broadcast;

import akka.routing.BroadcastPool;
import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.BroadcastRouter;

public class Example {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ActorSystem _system = ActorSystem.create("BroadcastRouterExample");
        ActorRef broadcastRouter = _system.actorOf(Props.create(MsgEchoActor.class)
                .withRouter(new BroadcastPool(5)), "myBroadcastRouterActor");

        for (int i = 1; i <= 2; i++) {
            //same message goes to all the actors
            broadcastRouter.tell(i, ActorRef.noSender());
        }
        _system.shutdown();

    }

}
