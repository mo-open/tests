package org.akka.essentials.stm.java.transactor.example;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;
import static akka.pattern.Patterns.ask;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;
import akka.transactor.CoordinatedTransactionException;
import org.akka.essentials.stm.java.transactor.example.msg.AccountBalance;
import org.akka.essentials.stm.java.transactor.example.msg.TransferMsg;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

public class BankActor extends UntypedActor {

    ActorRef transfer = getContext().actorOf(Props.create(TransferActor.class),
            "TransferActor");

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TransferMsg) {
            transfer.tell(message, getSelf());
        } else if (message instanceof AccountBalance) {
            AccountBalance account = (AccountBalance) Await.result(
                    ask(transfer, message, 5000), Duration.apply("5 second"));

            System.out.println("Account #" + account.getAccountNumber()
                    + " , Balance " + account.getBalance());

            getSender().tell(account, getSelf());
        }

    }

    // catch the exceptions and apply the right strategy, in this case resume()
    private static SupervisorStrategy strategy = new OneForOneStrategy(10,
            Duration.apply("10 second"), new Function<Throwable, Directive>() {

        public Directive apply(Throwable t) {
            if (t instanceof CoordinatedTransactionException) {
                return resume();
            } else if (t instanceof IllegalStateException) {
                return stop();
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

}
