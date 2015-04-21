/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import akka.actor.UntypedActor;
import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.actor.ActorRegistry;

public class UseAccountService {
  public static void printBalance(
    final String accountName, final ActorRef account) {

    Balance balance = 
      (Balance)(account.sendRequestReply(new FetchBalance()));
    System.out.println(accountName + " balance is " + balance.amount);
  }
  
  public static void main(final String[] args) 
    throws InterruptedException {
    final ActorRef account1 = Actors.actorOf(Account.class).start();
    final ActorRef account2 = Actors.actorOf(Account.class).start();
    final ActorRef accountService = 
      Actors.actorOf(AccountService.class).start();
    
    account1.sendOneWay(new Deposit(1000));
    account2.sendOneWay(new Deposit(1000));
    
    Thread.sleep(1000);

    printBalance("Account1", account1);
    printBalance("Account2", account2);
    
    System.out.println("Let's transfer $20... should succeed");
    accountService.sendOneWay(new Transfer(account1, account2, 20));

    Thread.sleep(1000);

    printBalance("Account1", account1);
    printBalance("Account2", account2);

    System.out.println("Let's transfer $2000... should not succeed");
    accountService.sendOneWay(new Transfer(account1, account2, 2000));

    Thread.sleep(6000);

    printBalance("Account1", account1);
    printBalance("Account2", account2);
   
    Actors.registry().shutdownAll();
  }
}
