package org.akka.essentials.supervisor.java.example2;

import java.util.concurrent.TimeUnit;

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
    TestActorRef<SupervisorActor2> supervisor = TestActorRef.apply(Props.create(
            SupervisorActor2.class), _system);

    public SupervisorTest() {
        super(_system);
    }

    @Test
    public void successTest() throws Exception {
        supervisor.tell(Integer.valueOf(8), ActorRef.noSender());

        Integer result = (Integer) Await.result(
                Patterns.ask(supervisor, new MyActorSystem2.Result(), 5000),
                Duration.create(5000, TimeUnit.MILLISECONDS));

        assert result.equals(Integer.valueOf(8));
    }

    @Test
    public void resumeTest() throws Exception {
        supervisor.tell(Integer.valueOf(-8), ActorRef.noSender());

        Integer result = (Integer) Await.result(
                Patterns.ask(supervisor, new MyActorSystem2.Result(), 5000),
                Duration.create(5000, TimeUnit.MILLISECONDS));

        assert result.equals(Integer.valueOf(8));
    }

    @Test
    public void restartTest() throws Exception {
        supervisor.tell(null, ActorRef.noSender());

        Integer result = (Integer) Await.result(
                Patterns.ask(supervisor, new MyActorSystem2.Result(), 5000),
                Duration.create(5000, TimeUnit.MILLISECONDS));

        assert result.equals(Integer.valueOf(0));
    }

    @Test
    public void stopTest() throws Exception {

        ActorRef workerActor1 = supervisor.underlyingActor().workerActor1;
        ActorRef workerActor2 = supervisor.underlyingActor().workerActor2;

        TestProbe probe = new TestProbe(_system);
        probe.watch(workerActor1);
        probe.watch(workerActor2);

        supervisor.tell(String.valueOf("Do Something"), ActorRef.noSender());

        probe.expectMsg(new Terminated(workerActor1, true, true));
        probe.expectMsg(new Terminated(workerActor2, true, true));
    }
}
