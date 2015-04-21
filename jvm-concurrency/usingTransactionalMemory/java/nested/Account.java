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

public class Account {
  final private Ref<Integer> balance = new Ref<Integer>();
  
  public Account(int initialBalance) { balance.swap(initialBalance); }
  
  public int getBalance() { return balance.get(); }

  public void deposit(final int amount) {
    new Atomic<Boolean>() {
      public Boolean atomically() {
        System.out.println("Deposit " + amount);
        if (amount > 0) {
          balance.swap(balance.get() + amount);
          return true;          
        }
        
        throw new AccountOperationFailedException();
      }  
    }.execute();
  }

  public void withdraw(final int amount) {
    new Atomic<Boolean>() {
      public Boolean atomically() {
        int currentBalance = balance.get();
        if (amount > 0 && currentBalance >= amount) {
          balance.swap(currentBalance - amount);
          return true;
        }
        
        throw new AccountOperationFailedException();
      }  
    }.execute();
  }
}
