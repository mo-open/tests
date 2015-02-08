package org.akka.essentials.dispatcher.java.example.Dispatcher;

import akka.routing.RoundRobinPool;
import org.akka.essentials.dispatcher.java.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.ConfigFactory;

public class Example2 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ActorSystem _system = ActorSystem.create("default-dispatcher",
                ConfigFactory.load().getConfig("MyDispatcherExample"));

        ActorRef actor = _system.actorOf(Props.create(MsgEchoActor.class)
                .withDispatcher("defaultDispatcher1").withRouter(
                        new RoundRobinPool(5)));

        for (int i = 0; i < 25; i++) {
            actor.tell(i, ActorRef.noSender());
        }

        _system.shutdown();

    }

}
