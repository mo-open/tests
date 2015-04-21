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
import akka.stm.TransactionalMap;
import static scala.collection.JavaConversions.asJavaIterable;

public class Scores {
  final private TransactionalMap<String, Integer> scoreValues = 
    new TransactionalMap<String, Integer>();
  final private Ref<Long> updates = new Ref<Long>(0L);
  
  public void updateScore(final String name, final int score) {
    new Atomic() {
      public Object atomically() {
        scoreValues.put(name, score);
        updates.swap(updates.get() + 1);
        if (score == 13) 
          throw new RuntimeException("Reject this score");
        return null;
      }
    }.execute();
  }

  public Iterable<String> getNames() {
    return asJavaIterable(scoreValues.keySet());
  }    
  
  public long getNumberOfUpdates() { return updates.get(); }

  public int getScore(final String name) { 
    return scoreValues.get(name).get(); 
  }
}
