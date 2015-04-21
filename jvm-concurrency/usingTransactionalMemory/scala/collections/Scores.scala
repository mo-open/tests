package com.agiledeveloper.pcj

import akka.stm.{Ref, atomic, TransactionalMap}

//START:CODE
class Scores {
  private val scoreValues = new TransactionalMap[String, Int]()
  private val updates = Ref(0L)
  
  def updateScore(name : String, score : Int) = {
    atomic {
      scoreValues.put(name, score)
      updates.swap(updates.get() + 1)
      if (score == 13) throw new RuntimeException("Reject this score")
    }
  }

  def foreach(codeBlock : ((String, Int)) => Unit) = 
    scoreValues.foreach(codeBlock)
  
  def getNumberOfUpdates() = updates.get()
}
//END:CODE
