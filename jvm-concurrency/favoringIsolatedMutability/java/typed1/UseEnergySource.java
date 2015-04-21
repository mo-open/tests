/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;

import akka.actor.TypedActor;
import akka.actor.ActorRef;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class UseEnergySource {
  public static void main(final String[] args) 
    throws InterruptedException { 
    System.out.println("Thread in main: " + 
      Thread.currentThread().getName());
    
    final EnergySource energySource = 
      TypedActor.newInstance(EnergySource.class, EnergySourceImpl.class);

    System.out.println("Energy units " + energySource.getUnitsAvailable());

    System.out.println("Firing two requests for use energy");
    energySource.useEnergy(10);
    energySource.useEnergy(10);
    System.out.println("Fired two requests for use energy");
    Thread.sleep(100);
    System.out.println("Firing one more requests for use energy");
    energySource.useEnergy(10);

    Thread.sleep(1000);
    System.out.println("Energy units " + energySource.getUnitsAvailable());
    System.out.println("Usage " + energySource.getUsageCount());
    
    TypedActor.stop(energySource);
  }
}
