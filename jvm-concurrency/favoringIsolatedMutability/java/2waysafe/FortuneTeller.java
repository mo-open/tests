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
import akka.actor.Actors;
import akka.actor.ActorRef;
import akka.actor.ActorTimeoutException;

public class FortuneTeller extends UntypedActor {
  public void onReceive(final Object name) {
    if(getContext().replySafe(String.format("%s you'll rock", name)))
      System.out.println("Message sent for " + name);
    else
      System.out.println("Sender not found for " + name);
  }  

  public static void main(final String[] args) {
    final ActorRef fortuneTeller = 
      Actors.actorOf(FortuneTeller.class).start();
    
    try {
      fortuneTeller.sendOneWay("Bill");
      final Object response = fortuneTeller.sendRequestReply("Joe");
      System.out.println(response);
    } catch(ActorTimeoutException ex) {
      System.out.println("Never got a response before timeout");      
    } finally {
      fortuneTeller.stop();      
    }
  }
}
