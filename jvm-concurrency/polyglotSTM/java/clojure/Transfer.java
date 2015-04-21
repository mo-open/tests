/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import clojure.lang.LockingTransaction;
import java.util.concurrent.Callable;

public class Transfer {
  public static void transfer(
    final Account from, final Account to, final int amount) 
    throws Exception {
    LockingTransaction.runInTransaction(new Callable<Boolean>() {
      public Boolean call() throws Exception {
        to.deposit(amount);
        from.withdraw(amount);
        return true;
      }
    });
  }

  public static void transferAndPrint(
    final Account from, final Account to, final int amount) {
    try {
      transfer(from, to, amount);
    } catch(Exception ex) { 
      System.out.println("transfer failed " + ex);
    }

    System.out.println("Balance of from account is " + from.getBalance());
    System.out.println("Balance of to account is " + to.getBalance());
  }

  public static void main(final String[] args) throws Exception { 
    final Account account1 = new Account(2000);
    final Account account2 = new Account(100);

    transferAndPrint(account1, account2, 500);
    transferAndPrint(account1, account2, 5000);
  }
}
