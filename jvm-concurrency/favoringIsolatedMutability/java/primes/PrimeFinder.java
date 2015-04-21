/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

public class PrimeFinder {
  public static boolean isPrime(final int number) {
    if (number <= 1) return false;
    final int limit = (int) Math.sqrt(number);
    for(int i = 2; i <= limit; i++) if(number % i == 0) return false;
    return true;
  }
  public static int countPrimesInRange(final int lower, final int upper) {    
    int count = 0;
    for(int index = lower; index <= upper; index++)
      if(isPrime(index)) count += 1;
    return count;
  }  
}
