package com.agiledeveloper.pcj

import akka.actor.Actor

//START:CODE
class FortuneTeller extends Actor {
  def receive = {
    case name : String =>
      if(self.reply_?(String.format("%s you'll rock", name)))
        println("Message sent for " + name)
      else
        println("Sender not found for " + name)
  }  
}
object FortuneTeller {
  def main(args : Array[String]) : Unit = {
    val fortuneTeller = Actor.actorOf[FortuneTeller].start()
    fortuneTeller ! "Bill"
    val response = fortuneTeller !! "Joe"
    response match {
      case Some(responseMessage) => println(responseMessage)
      case None => println("Never got a response before timeout")
    }
    fortuneTeller.stop()
  }
}
//END:CODE
