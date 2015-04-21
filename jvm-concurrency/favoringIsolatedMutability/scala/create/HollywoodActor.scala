package com.agiledeveloper.pcj

import akka.actor.Actor

//START:CODE
class HollywoodActor extends Actor {
  def receive = {
    case role => 
    println("Playing " + role + 
      " from Thread " + Thread.currentThread().getName())
  }  
}
//END:CODE
