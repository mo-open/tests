/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import akka.actor.UntypedActor;
import akka.actor.Actors;
import java.io.File;

public class Monitor extends UntypedActor {
  public void onReceive(Object message) {
    System.out.println(message);
  }  

  public static void main(final String[] args) {    
    Actors.remote().start("localhost", 8000)
      .register("system-monitor", Actors.actorOf(Monitor.class));

    System.out.println("Press key to stop");
    System.console().readLine();
    Actors.registry().shutdownAll();
    Actors.remote().shutdown();
  }
}
