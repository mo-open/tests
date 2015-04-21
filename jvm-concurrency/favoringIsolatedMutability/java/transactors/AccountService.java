/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import akka.transactor.UntypedTransactor;
import akka.actor.ActorRef;
import akka.transactor.SendTo;
import java.util.Set;

public class AccountService extends UntypedTransactor {
  
  @Override public Set<SendTo> coordinate(final Object message) { 
    if(message instanceof Transfer) {
      Set<SendTo> coordinations = new java.util.HashSet<SendTo>();
      Transfer transfer = (Transfer) message;      
      coordinations.add(sendTo(transfer.to, new Deposit(transfer.amount)));
      coordinations.add(sendTo(transfer.from, 
        new Withdraw(transfer.amount)));
      return java.util.Collections.unmodifiableSet(coordinations);
    }

    return nobody();
  }
  
  public void atomically(final Object message) {}
}
