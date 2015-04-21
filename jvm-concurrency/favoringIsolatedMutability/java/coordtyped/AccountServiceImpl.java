/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import akka.actor.TypedActor;
import akka.transactor.Atomically;
import static akka.transactor.Coordination.coordinate;

public class AccountServiceImpl 
  extends TypedActor implements AccountService {

  public void transfer(
    final Account from, final Account to, final int amount) {

    coordinate(true, new Atomically() {
      public void atomically() {
        to.deposit(amount);
        from.withdraw(amount);
      }
    });
  }
}
