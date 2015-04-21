package com.agiledeveloper.pcj

import akka.actor.Actor

//START:CODE
object UseHollywoodActor {
  def main(args : Array[String]) : Unit = {

    val tomHanks = Actor.actorOf(new HollywoodActor("Hanks")).start()

    tomHanks ! "James Lovell"
    tomHanks ! new StringBuilder("Politics")
    tomHanks ! "Forrest Gump"
    Thread.sleep(1000)
    tomHanks.stop()
  }
}
//END:CODE
