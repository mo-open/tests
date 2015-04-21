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

public class Portfolio {
  final private Ref<Integer> checkingBalance = new Ref<Integer>(500);
  final private Ref<Integer> savingsBalance = new Ref<Integer>(600);
  
  public int getCheckingBalance() { return checkingBalance.get(); }
  public int getSavingsBalance() { return savingsBalance.get(); }
  
  public void withdraw(final boolean fromChecking, final int amount) {
    new Atomic<Object>() { 
      public Object atomically() {
        final int totalBalance = 
          checkingBalance.get() + savingsBalance.get();
        try { Thread.sleep(1000); } catch(InterruptedException ex) {}
        if(totalBalance - amount >= 1000) {
          if(fromChecking)
            checkingBalance.swap(checkingBalance.get() - amount);
          else
            savingsBalance.swap(savingsBalance.get() - amount);
        }
        else
          System.out.println(
            "Sorry, can't withdraw due to constraint violation");
        return null;
      }        
    }.execute();
  }
}
