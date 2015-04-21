/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import akka.stm.Ref;
import akka.stm.Atomic;
import static akka.stm.StmUtils.deferred;
import static akka.stm.StmUtils.compensating;

public class Counter {
  private final Ref<Integer> value = new Ref<Integer>(1);
      
  public void decrement() {
    new Atomic<Integer>() {
      public Integer atomically() {
        
        deferred(new Runnable() {
          public void run() {
            System.out.println(
              "Transaction completed...send email, log, etc.");
          }  
        });

        compensating(new Runnable() {
          public void run() {
            System.out.println("Transaction aborted...hold the phone");
          }  
        });

        if(value.get() <= 0) 
          throw new RuntimeException("Operation not allowed");
                     
        value.swap(value.get() - 1);
        return value.get();        
      }
    }.execute();
  }
}
