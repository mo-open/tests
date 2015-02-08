package org.akka.essentials.stm.java.transactor.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.akka.essentials.stm.java.transactor.example.msg.AccountBalance;
import org.akka.essentials.stm.java.transactor.example.msg.TransferMsg;

public class BankApplication {
    ActorSystem _system = ActorSystem.apply("STM-Example");
    ActorRef bank = _system.actorOf(Props.create(BankActor.class), "BankActor");

    public static void main(String args[]) {

        BankApplication bankApp = new BankApplication();

        bankApp.showBalances();

        bankApp.bank.tell(new TransferMsg(Float.valueOf("1500")), ActorRef.noSender());

        bankApp.showBalances();

        bankApp.bank.tell(new TransferMsg(Float.valueOf("1400")), ActorRef.noSender());

        bankApp.showBalances();

        bankApp.bank.tell(new TransferMsg(Float.valueOf("3500")), ActorRef.noSender());

        bankApp.showBalances();

        bankApp._system.shutdown();

    }

    private void showBalances() {
        try {
            Thread.sleep(2000);
            bank.tell(new AccountBalance("XYZ"), ActorRef.noSender());
            bank.tell(new AccountBalance("ABC"), ActorRef.noSender());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}