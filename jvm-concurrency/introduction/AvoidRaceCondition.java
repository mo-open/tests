/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

public class AvoidRaceCondition {            
  private static volatile boolean done;
  
  public static void main(final String[] args) throws Exception {
      new Thread(
              new Runnable() {
                  public void run() {
  	              int i = 0;
                      while(!done) { i++; }
                      System.out.println("Done!");
                  }
              }
      ).start();

      System.out.println("OS: " + System.getProperty("os.name"));  
      Thread.sleep(2000);
      done = true;
      System.out.println("flag done set to true");
  }
}
