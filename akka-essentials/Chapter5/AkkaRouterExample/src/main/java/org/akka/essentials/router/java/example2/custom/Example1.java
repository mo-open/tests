package org.akka.essentials.router.java.example2.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import akka.routing.RandomGroup;
import akka.routing.RandomPool;
import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RandomRouter;

public class Example1 {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ActorSystem _system = ActorSystem.create("CustomRouteeRouterExample");

        ActorRef echoActor1 = _system.actorOf(Props.create(MsgEchoActor.class));
        ActorRef echoActor2 = _system.actorOf(Props.create(MsgEchoActor.class));
        ActorRef echoActor3 = _system.actorOf(Props.create(MsgEchoActor.class));

        Iterable<ActorRef> routees = Arrays.asList(new ActorRef[]{echoActor1,
                echoActor2, echoActor3});
        List<String> routeePaths = new ArrayList<String>();
        routeePaths.add(echoActor1.path().toStringWithoutAddress());
        routeePaths.add(echoActor2.path().toStringWithoutAddress());
        routeePaths.add(echoActor3.path().toStringWithoutAddress());

        ActorRef randomRouter = _system.actorOf(Props.create(MsgEchoActor.class)
                .withRouter(new RandomGroup(routeePaths)));

        for (int i = 1; i <= 10; i++) {
            // sends randomly to actors
            randomRouter.tell(i, ActorRef.noSender());
        }
        _system.shutdown();
    }

}
