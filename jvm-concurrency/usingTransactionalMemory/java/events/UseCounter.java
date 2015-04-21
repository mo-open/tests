/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

public class UseCounter {
  public static void main(final String[] args) {
    Counter counter = new Counter();
    counter.decrement();
    
    System.out.println("Let's try again...");
    try {
      counter.decrement();      
    } catch(Exception ex) {
      System.out.println(ex.getMessage());
    }
  }
}
