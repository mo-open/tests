/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package com.agiledeveloper.pcj;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EnergySource {
  private final long MAXLEVEL = 100;
  private long level = MAXLEVEL;
  private static final ScheduledExecutorService replenishTimer =
    Executors.newScheduledThreadPool(10);
  private ScheduledFuture<?> replenishTask;

  private EnergySource() {}
  
  private void init() {   
    replenishTask = replenishTimer.scheduleAtFixedRate(new Runnable() {
      public void run() { replenish(); }
		}, 0, 1, TimeUnit.SECONDS);
  }

  public static EnergySource create() {
    final EnergySource energySource = new EnergySource();
    energySource.init();
    return energySource;
  }

//Ensure visibility...other issues pending
  //...
  public synchronized long getUnitsAvailable() { return level; }

  public synchronized boolean useEnergy(final long units) {
    if (units > 0 && level >= units) {
      level -= units;
      return true;
    }
    return false;
  }

  public synchronized void stopEnergySource() { 
    replenishTask.cancel(false); 
  }

  private synchronized void replenish() { if (level < MAXLEVEL) level++; }
}
