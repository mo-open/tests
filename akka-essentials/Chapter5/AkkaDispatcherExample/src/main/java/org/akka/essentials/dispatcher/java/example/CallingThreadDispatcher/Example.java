package org.akka.essentials.dispatcher.java.example.CallingThreadDispatcher;

import akka.routing.RoundRobinPool;
import org.akka.essentials.dispatcher.java.MsgEchoActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.ConfigFactory;

public class Example {
    /**
     * @param args
     */
    public static void main(String[] args) {
        ActorSystem _system = ActorSystem.create("callingThread-dispatcher",
                ConfigFactory.load().getConfig("MyDispatcherExample"));

        ActorRef actor = _system.actorOf(Props.create(MsgEchoActor.class)
                .withDispatcher("CallingThreadDispatcher").withRouter(
                        new RoundRobinPool(2)));

        for (int i = 0; i < 5; i++) {
            actor.tell(i, ActorRef.noSender());
        }

        _system.shutdown();

    }
}
