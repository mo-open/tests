package com.agiledeveloper.pcj

import akka.actor.Actor

//START:CODE
class HollywoodActor(val name : String) extends Actor {
  def receive = {
    case role : String => println(String.format("%s playing %s", name, role))
    case msg => println(name + " plays no " + msg)
  }  
}
//END:CODE
