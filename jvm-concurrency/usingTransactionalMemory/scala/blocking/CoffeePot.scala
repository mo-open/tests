package com.agiledeveloper.pcj

import akka.stm.Ref
import akka.stm.atomic
import akka.stm.retry
import akka.stm.TransactionFactory
import akka.stm.TransactionFactoryBuilder
import java.util.Timer
import java.util.TimerTask
import akka.util.duration.intToDurationInt

//START:CODE
object CoffeePot {
  val start = System.nanoTime()
  val cups = Ref(24)
  
  def fillCup(numberOfCups : Int) = { 
    val factory = TransactionFactory(blockingAllowed = true, 
      timeout = 6 seconds)

    atomic(factory) {
      if(cups.get() < numberOfCups) {
        println("retry........ at " + (System.nanoTime() - start)/1.0e9) 
        retry()
      }
      cups.swap(cups.get() - numberOfCups)
      println("filled up...." + numberOfCups)
      println("........ at " + (System.nanoTime() - start)/1.0e9)       
    }      
  }

  def main(args : Array[String]) : Unit = { 
    val timer = new Timer(true)
    timer.schedule(new TimerTask() {
      def run() { 
        println("Refilling.... at " + (System.nanoTime() - start)/1.0e9)
        cups.swap(24)
      }
    }, 5000)
    
    fillCup(20)
    fillCup(10)
    try {
      fillCup(22)      
    } catch {
      case ex => println("Failed: " + ex.getMessage())
    }
  }
}
//END:CODE
