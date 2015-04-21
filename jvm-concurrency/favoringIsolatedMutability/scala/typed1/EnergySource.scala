package com.agiledeveloper.pcj

//START:CODE
trait EnergySource {
  def getUnitsAvailable() : Long
  def getUsageCount() : Long
  def useEnergy(units : Long) : Unit
}
//END:CODE
