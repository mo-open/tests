package com.agiledeveloper.pcj

import akka.actor.Actor
import akka.actor.Actors
import akka.actor.ActorRegistry
import akka.dispatch.Future
import java.lang.Integer 

//START:CODE
class Primes extends Actor {
  def receive = {
    case (lower : Int, upper : Int) => 
      val count = PrimeFinder.countPrimesInRange(lower, upper)
      self.replySafe(new Integer(count))
  }    
}

object Primes {
  def countPrimes(number : Int, numberOfParts : Int) = {
    val chunksPerPartition : Int = number / numberOfParts
    
    val results = new Array[Future[Integer]](numberOfParts)
    var index = 0
    
    while(index < numberOfParts) {
      val lower = index * chunksPerPartition + 1
      val upper = if (index == numberOfParts - 1) 
        number else lower + chunksPerPartition - 1
      val bounds = (lower, upper)
      val primeFinder = Actor.actorOf[Primes].start()
      results(index) = (primeFinder !!! bounds).asInstanceOf[Future[Integer]]
      index += 1
    }

    var count = 0
    index = 0
    while(index < numberOfParts) {
      count += results(index).await.result.get.intValue()
      index += 1
    }    
    Actors.registry.shutdownAll
    count
  }

  def main(args : Array[String]) : Unit = {
    if (args.length < 2) 
      println("Usage: number numberOfParts")    
    else {
      val start = System.nanoTime
      val count = countPrimes(args(0).toInt, args(1).toInt)
      val end = System.nanoTime
      println("Number of primes is " + count)
      println("Time taken " + (end - start)/1.0e9)
    }
  }
}
//END:CODE
