/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import akka.stm.Atomic;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class AccountService {
  public void transfer(
    final Account from, final Account to, final int amount) {
    new Atomic<Boolean>() {
       public Boolean atomically() {
         System.out.println("Attempting transfer...");
         to.deposit(amount);
         System.out.println("Simulating a delay in transfer...");
         try { Thread.sleep(5000); } catch(Exception ex) {}
         System.out.println("Uncommitted balance after deposit $" +
           to.getBalance());
         from.withdraw(amount);
         return true;
       }
    }.execute();
  }

  public static void transferAndPrintBalance(
   final Account from, final Account to, final int amount) {
    boolean result = true;
    try {
      new AccountService().transfer(from, to, amount);
    } catch(AccountOperationFailedException ex) {
      result = false;
    }

    System.out.println("Result of transfer is " + (result ? "Pass" : "Fail"));
    System.out.println("From account has $" + from.getBalance());
    System.out.println("To account has $" + to.getBalance());
  } 

  public static void main(final String[] args) throws Exception { 
    final Account account1 = new Account(2000);
    final Account account2 = new Account(100);

    final ExecutorService service = Executors.newSingleThreadExecutor();
    service.submit(new Runnable() {
      public void run() {
        try { Thread.sleep(1000); } catch(Exception ex) {}
        account2.deposit(20);
      }
    });
    service.shutdown();
    
    transferAndPrintBalance(account1, account2, 500);
    
    System.out.println("Making large transfer...");
    transferAndPrintBalance(account1, account2, 5000);
  }
}
