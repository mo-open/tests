/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class UsePortfolio {
  public static void main(final String[] args) throws InterruptedException {
    final Portfolio portfolio = new Portfolio(); 
    
    int checkingBalance = portfolio.getCheckingBalance();
    int savingBalance = portfolio.getSavingsBalance();
    System.out.println("Checking balance is " + checkingBalance);
    System.out.println("Savings balance is " + savingBalance);
    System.out.println("Total balance is " + 
      (checkingBalance + savingBalance));
    
    final ExecutorService service = Executors.newFixedThreadPool(10);
    service.execute(new Runnable() {
      public void run() { portfolio.withdraw(true, 100); }
    });
    service.execute(new Runnable() {
      public void run() { portfolio.withdraw(false, 100); }
    });
    
    service.shutdown();
    
    Thread.sleep(4000);
          
    checkingBalance = portfolio.getCheckingBalance();
    savingBalance = portfolio.getSavingsBalance();
    System.out.println("Checking balance is " + checkingBalance);
    System.out.println("Savings balance is " + savingBalance);
    System.out.println("Total balance is " + 
      (checkingBalance + savingBalance));
    if(checkingBalance + savingBalance < 1000) 
      System.out.println("Oops, broke the constraint!");
  }
}
