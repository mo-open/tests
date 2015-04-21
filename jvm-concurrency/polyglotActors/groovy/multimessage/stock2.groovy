/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
@Immutable class LookUp {
  String ticker
}

@Immutable class Buy {
  String ticker
  int quantity
}

import groovyx.gpars.actor.DynamicDispatchActor

class Trader extends DynamicDispatchActor {
  void onMessage(Buy message) {
    println "Buying ${message.quantity} shares of ${message.ticker}"    
  }
  
  def onMessage(LookUp message) {
    sender.send((int)(Math.random() * 1000))    
  }
}

trader = new Trader().start()
trader.sendAndContinue(new LookUp("XYZ")) {
  println "Price of XYZ sock is $it"
}

trader << new Buy("XYZ", 200)

trader.join(1, java.util.concurrent.TimeUnit.SECONDS)
