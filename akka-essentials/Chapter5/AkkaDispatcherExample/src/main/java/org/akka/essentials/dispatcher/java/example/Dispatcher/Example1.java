package org.akka.essentials.dispatcher.java.example.Dispatcher;

import akka.routing.RoundRobinPool;
import org.akka.essentials.dispatcher.java.MsgEchoActor;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinRouter;

/**
 * Hello world!
 */
public class Example1 {
    public static void main(String[] args) {
        ActorSystem _system = ActorSystem.create("default-dispatcher",
                ConfigFactory.load().getConfig("MyDispatcherExample"));

        ActorRef actor = _system.actorOf(Props.create(MsgEchoActor.class)
                .withDispatcher("defaultDispatcher").withRouter(
                        new RoundRobinPool(5)));

        for (int i = 0; i < 25; i++) {
            actor.tell(i, ActorRef.noSender());
        }

        _system.shutdown();
    }
}
