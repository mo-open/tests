package org.akka.essentials.supervisor.java.example3;

import java.util.concurrent.TimeUnit;

import org.akka.essentials.supervisor.example3.Result;
import org.akka.essentials.supervisor.example3.SupervisorActor;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.pattern.Patterns;
import akka.testkit.TestActorRef;
import akka.testkit.TestKit;
import akka.testkit.TestProbe;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

public class SupervisorTest extends TestKit {
    static ActorSystem _system = ActorSystem.create("faultTolerance");

    public SupervisorTest() {
        super(_system);
    }

    @Test
    public void stopAndRestartTest() throws Exception {
        TestActorRef<SupervisorActor> supervisor = TestActorRef.apply(
                Props.create(SupervisorActor.class), _system);
        ActorRef workerActor = supervisor.underlyingActor().childActor();
        TestProbe probe = new TestProbe(_system);
        probe.watch(workerActor);
        supervisor.tell("10", ActorRef.noSender());
        probe.expectMsg(new Terminated(workerActor, true, true));

        // the actor should get restarted
        // lets send a new value and retrieve the same
        supervisor.tell(Integer.valueOf(10), ActorRef.noSender());

        Integer result = (Integer) Await.result(
                Patterns.ask(supervisor, new Result(), 5000),
                Duration.create(5000, TimeUnit.MILLISECONDS));

        assert result.equals(Integer.valueOf(10));
    }

}
