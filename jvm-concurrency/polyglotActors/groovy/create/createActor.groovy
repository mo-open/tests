/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
import groovyx.gpars.actor.DefaultActor

class HollywoodActor extends DefaultActor {
  def name
  
  public HollywoodActor(actorName) { name = actorName }
  
  void act() {
    loop {
      react { role ->
        println "$name playing the role $role"
        println "$name runs in ${Thread.currentThread()}"
      }
    }
  }
}


depp = new HollywoodActor("Johnny Depp").start()
hanks = new HollywoodActor("Tom Hanks").start()

depp.send("Wonka")
hanks.send("Lovell")

depp << "Sparrow"
hanks "Gump"

[depp, hanks]*.join(1, java.util.concurrent.TimeUnit.SECONDS)
