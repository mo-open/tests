package com.agiledeveloper.pcj

import scala.actors.Actor
import scala.actors.Actor._

//START:CODE
object UseEnergySource {
  val energySource = EnergySource.create()
  
  def main(args : Array[String]) {
    println("Energy level at start: " + energySource.getUnitsAvailable())

    val caller = self
    for(i <- 1 to 10) actor {
      for(j <- 1 to 7) energySource.useEnergy(1)
      caller ! true
    }        
    
    for(i <- 1 to 10) { receiveWithin(1000) { case message => } }

    println("Energy level at end: " + energySource.getUnitsAvailable())
    println("Usage: " + energySource.getUsageCount())

    energySource.stopEnergySource()    
  }
}
//END:CODE
