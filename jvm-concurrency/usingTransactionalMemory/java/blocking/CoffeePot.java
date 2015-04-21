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
import akka.stm.TransactionFactory;
import akka.stm.TransactionFactoryBuilder;
import java.util.Timer;
import java.util.TimerTask;
import akka.util.DurationInt;
import static akka.stm.StmUtils.retry;

public class CoffeePot {
  private static final long start = System.nanoTime();
  private static final Ref<Integer> cups = new Ref<Integer>(24);
  
  private static void fillCup(final int numberOfCups) { 
    final TransactionFactory factory = 
      new TransactionFactoryBuilder()
      .setBlockingAllowed(true)
      .setTimeout(new DurationInt(6).seconds())
      .build();
      
    new Atomic<Object>(factory) {
      public Object atomically() {
        if(cups.get() < numberOfCups) {
          System.out.println("retry........ at " + 
            (System.nanoTime() - start)/1.0e9); 
          retry();
        }
        cups.swap(cups.get() - numberOfCups);
        System.out.println("filled up...." + numberOfCups);
        System.out.println("........ at " + 
          (System.nanoTime() - start)/1.0e9); 
        return null;
      }
    }.execute();
  }

  public static void main(final String[] args) { 
    final Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {
      public void run() { 
        System.out.println("Refilling.... at " + 
          (System.nanoTime() - start)/1.0e9);
        cups.swap(24);
      }
    }, 5000);
    
    fillCup(20);
    fillCup(10);
    try {
      fillCup(22);      
    } catch(Exception ex) {
      System.out.println("Failed: " + ex.getMessage());
    }
  }
}
