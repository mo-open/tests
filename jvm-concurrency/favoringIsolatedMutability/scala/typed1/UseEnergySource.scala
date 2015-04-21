package com.agiledeveloper.pcj

import akka.actor.Actor
import akka.actor.TypedActor

//START:CODE
object UseEnergySource {
  def main(args : Array[String]) : Unit = {
    println("Thread in main: " + Thread.currentThread().getName())
    
    val energySource = TypedActor.newInstance(
      classOf[EnergySource], classOf[EnergySourceImpl])

    println("Energy units " + energySource.getUnitsAvailable)

    println("Firing two requests for use energy")
    energySource.useEnergy(10)
    energySource.useEnergy(10)
    println("Fired two requests for use energy")
    Thread.sleep(100)
    println("Firing one more requests for use energy")
    energySource.useEnergy(10)

    Thread.sleep(1000);
    println("Energy units " + energySource.getUnitsAvailable)
    println("Usage " + energySource.getUsageCount)
    
    TypedActor.stop(energySource)
  }
}
//END:CODE
