package org.akka.essentials.stm.java.transactor.example;

import static akka.pattern.Patterns.ask;

import akka.actor.ActorRef;
import org.akka.essentials.stm.java.transactor.example.msg.AccountBalance;
import org.akka.essentials.stm.java.transactor.example.msg.TransferMsg;
import org.junit.Assert;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.TestKit;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

public class BankTest extends TestKit {
    static ActorSystem _system = ActorSystem.create("STM-Example");

    public BankTest() {
        super(_system);
    }

    @Test
    public void successTest() throws Exception {
        TestActorRef<BankActor> bank = TestActorRef.apply(Props.create(
                BankActor.class), _system);

        bank.tell(new TransferMsg(Float.valueOf("1777")), ActorRef.noSender());

        AccountBalance balance = (AccountBalance) Await.result(
                ask(bank, new AccountBalance("XYZ"), 5000),
                Duration.apply("5 second"));

        Assert.assertEquals(Float.parseFloat("3223"), balance.getBalance(),
                Float.parseFloat("0"));

        balance = (AccountBalance) Await.result(
                ask(bank, new AccountBalance("ABC"), 5000),
                Duration.apply("5 second"));

        Assert.assertEquals(Float.parseFloat("2777"), balance.getBalance(),
                Float.parseFloat("0"));
    }

    @Test
    public void failureTest() throws Exception {
        TestActorRef<BankActor> bank = TestActorRef.apply(Props.create(
                BankActor.class), _system);

        bank.tell(new TransferMsg(Float.valueOf("1500")), ActorRef.noSender());

        AccountBalance balance = (AccountBalance) Await.result(
                ask(bank, new AccountBalance("XYZ"), 5000),
                Duration.apply("5 second"));

        Assert.assertEquals(Float.parseFloat("3500"), balance.getBalance(),
                Float.parseFloat("0"));

        balance = (AccountBalance) Await.result(
                ask(bank, new AccountBalance("ABC"), 5000),
                Duration.apply("5 second"));

        Assert.assertEquals(Float.parseFloat("2500"), balance.getBalance(),
                Float.parseFloat("0"));

        bank.tell(new TransferMsg(Float.valueOf("4000")), ActorRef.noSender());

        Thread.sleep(2000);

        balance = (AccountBalance) Await.result(
                ask(bank, new AccountBalance("XYZ"), 5000),
                Duration.apply("5 second"));

        Assert.assertEquals(Float.parseFloat("3500"), balance.getBalance(),
                Float.parseFloat("0"));

        balance = (AccountBalance) Await.result(
                ask(bank, new AccountBalance("ABC"), 5000),
                Duration.apply("5 second"));

        Assert.assertEquals(Float.parseFloat("2500"), balance.getBalance(),
                Float.parseFloat("0"));
    }

}
