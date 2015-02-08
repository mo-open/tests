package org.akka.essentials.router.java.example2.custom;

import akka.remote.routing.RemoteRouterConfig;
import akka.routing.RandomPool;
import org.akka.essentials.router.example.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.routing.RandomRouter;

public class Example2 {
    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ActorSystem _system = ActorSystem.create("RemoteRouteeRouterExample");

        Address addr1 = new Address("akka", "remotesys", "127.0.0.1", 2552);
        Address addr2 = new Address("akka", "remotesys", "127.0.0.1", 2552);

        Address[] addresses = new Address[]{addr1, addr2};

        ActorRef randomRouter = _system.actorOf(Props.create(MsgEchoActor.class)
                .withRouter(new RemoteRouterConfig(new RandomPool(5), addresses)));

        for (int i = 1; i <= 10; i++) {
            // sends randomly to actors
            randomRouter.tell(i, ActorRef.noSender());
        }
        _system.shutdown();
    }

}
