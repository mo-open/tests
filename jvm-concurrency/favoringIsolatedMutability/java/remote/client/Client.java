/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import static akka.actor.Actors.remote;
import akka.actor.ActorRef;
import java.io.File;

public class Client {
  public static void main(final String[] args) {
    ActorRef systemMonitor = remote().actorFor(
      "system-monitor", "localhost", 8000);

    systemMonitor.sendOneWay("Cores: " + 
      Runtime.getRuntime().availableProcessors());
    systemMonitor.sendOneWay("Total Space: " + 
      new File("/").getTotalSpace());
    systemMonitor.sendOneWay("Free Space: " + 
      new File("/").getFreeSpace());
  }
}
