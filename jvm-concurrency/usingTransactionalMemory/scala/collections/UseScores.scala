package com.agiledeveloper.pcj

//START:CODE
object UseScores {
  def main(args : Array[String]) : Unit = { 
    val scores = new Scores()
    
    scores.updateScore("Joe", 14)
    scores.updateScore("Sally", 15)
    scores.updateScore("Bernie", 12)
    
    println("Number of updates: " + scores.getNumberOfUpdates())

    try {
      scores.updateScore("Bill", 13)
    } catch {
      case ex => println("update failed for score 13")
    }
    
    println("Number of updates: " + scores.getNumberOfUpdates())

    scores.foreach { mapEntry =>
      val (name, score) = mapEntry
      println("Score for " + name + " is " + score)
    }    
  }
}
//END:CODE
