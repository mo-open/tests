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

public class CoffeePot {
  private static final Ref<Integer> cups = new Ref<Integer>(24);
  
  public static int readWriteCups(final boolean write) { 
    final TransactionFactory factory = 
      new TransactionFactoryBuilder().setReadonly(true).build();
      
    return new Atomic<Integer>(factory) {
      public Integer atomically() { 
        if(write) cups.swap(20);
        return cups.get(); 
      }
    }.execute();
  }
  
  public static void main(final String[] args) {
    System.out.println("Read only");
    readWriteCups(false);

    System.out.println("Attempt to write");
    try {
      readWriteCups(true);      
    } catch(Exception ex) {
      System.out.println("Failed " + ex);
    }
  }
}
