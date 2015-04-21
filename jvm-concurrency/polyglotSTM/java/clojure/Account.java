/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import clojure.lang.Ref;
import clojure.lang.LockingTransaction;
import java.util.concurrent.Callable;

public class Account {
  final private Ref balance;

  public Account(final int initialBalance) throws Exception {
    balance = new Ref(initialBalance);
  }

  public int getBalance() { return (Integer) balance.deref(); }

  public void deposit(final int amount) throws Exception {
    LockingTransaction.runInTransaction(new Callable<Boolean>() {
      public Boolean call()  {
        if(amount > 0) {
          final int currentBalance = (Integer) balance.deref();
          balance.set(currentBalance + amount);
          System.out.println("deposit " + amount + "... will it stay");
          return true;
        } else throw new RuntimeException("Operation invalid");
      }
    });
  }

  public void withdraw(final int amount) throws Exception {
    LockingTransaction.runInTransaction(new Callable<Boolean>() {
      public Boolean call() {
        final int currentBalance = (Integer) balance.deref();
        if(amount > 0 && currentBalance >= amount) {
          balance.set(currentBalance - amount);
          return true;
        } else throw new RuntimeException("Operation invalid");
      }
    });
  }
}
