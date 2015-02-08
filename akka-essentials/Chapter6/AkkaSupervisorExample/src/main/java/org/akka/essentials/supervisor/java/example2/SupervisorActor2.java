package org.akka.essentials.supervisor.java.example2;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import akka.actor.ActorRef;
import akka.actor.AllForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class SupervisorActor2 extends UntypedActor {

    public ActorRef workerActor1;
    @SuppressWarnings("unused")
    public ActorRef workerActor2;

    public SupervisorActor2() {
        workerActor1 = getContext().actorOf(Props.create(WorkerActor1.class),
                "workerActor1");
        workerActor2 = getContext().actorOf(Props.create(WorkerActor2.class),
                "workerActor2");
    }

    private static SupervisorStrategy strategy = new AllForOneStrategy(10,
            Duration.apply(10, TimeUnit.SECONDS), new Function<Throwable, Directive>() {
        public Directive apply(Throwable t) {
            if (t instanceof ArithmeticException) {
                return resume();
            } else if (t instanceof NullPointerException) {
                return restart();
            } else if (t instanceof IllegalArgumentException) {
                return stop();
            } else {
                return escalate();
            }
        }
    });

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MyActorSystem2.Result) {
            workerActor1.tell(msg, getSender());
        } else
            workerActor1.tell(msg, getSender());
    }
}
