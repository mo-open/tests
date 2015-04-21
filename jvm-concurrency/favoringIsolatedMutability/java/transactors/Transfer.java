/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import akka.actor.ActorRef;

public class Transfer {
  public final ActorRef from;
  public final ActorRef to;
  public final int amount;
  public Transfer(final ActorRef fromAccount, 
    final ActorRef toAccount, final int theAmount) {
    from = fromAccount;
    to = toAccount;
    amount = theAmount;
  }
}
