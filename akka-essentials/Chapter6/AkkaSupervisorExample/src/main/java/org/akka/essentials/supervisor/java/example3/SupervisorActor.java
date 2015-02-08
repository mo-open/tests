package org.akka.essentials.supervisor.java.example3;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class SupervisorActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public ActorRef workerActor = getContext().actorOf(
            Props.create(WorkerActor.class), "workerActor");

    ActorRef monitor = getContext().system().actorOf(
            Props.create(MonitorActor.class), "monitorActor");

    @Override
    public void preStart() {
        //MyActorSystem.monitor.tell(new RegisterWorker(workerActor, self()));
        monitor.tell(new RegisterWorker(workerActor, self()), getSelf());
    }

    private static SupervisorStrategy strategy = new OneForOneStrategy(10,
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

    public void onReceive(Object o) throws Exception {
        if (o instanceof Result) {
            workerActor.tell(o, getSender());
        } else if (o instanceof DeadWorker) {
            log.info("Got a DeadWorker message, restarting the worker");
            workerActor = getContext().actorOf(Props.create(WorkerActor.class),
                    "workerActor");
        } else
            workerActor.tell(o, getSelf());
    }

    public ActorRef getWorker() {
        return workerActor;
    }
}
