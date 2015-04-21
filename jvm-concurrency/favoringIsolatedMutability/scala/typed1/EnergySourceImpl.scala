package com.agiledeveloper.pcj

import akka.actor.TypedActor

//START:CODE
class EnergySourceImpl extends TypedActor with EnergySource {
  val MAXLEVEL = 100L
  var level = MAXLEVEL
  var usageCount = 0L
  
  def getUnitsAvailable() = level
  
  def getUsageCount() = usageCount
  
  def useEnergy(units : Long) = {
    if (units > 0 && level - units >= 0) {
      println("Thread in useEnergy: " + Thread.currentThread().getName())
      level -= units
      usageCount += 1
    }
  }
}
//END:CODE
