package com.agiledeveloper.pcj

import akka.actor.{Actor, Actors, ActorRegistry}

//START:CODE
object UseHollywoodActor {
  def main(args : Array[String]) :Unit = { 
    val johnnyDepp = Actor.actorOf[HollywoodActor].start()    
    
    johnnyDepp ! "Jack Sparrow"
    Thread.sleep(100)
    johnnyDepp ! "Edward Scissorhands"
    Thread.sleep(100)
    johnnyDepp ! "Willy Wonka"

    Actors.registry.shutdownAll
  }
}
//END:CODE
