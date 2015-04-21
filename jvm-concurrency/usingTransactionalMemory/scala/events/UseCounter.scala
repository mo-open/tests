package com.agiledeveloper.pcj

//START:CODE
object UseCounter {
  def main(args : Array[String]) : Unit = {
    val counter = new Counter()
    counter.decrement()
    
    println("Let's try again...")
    try {
      counter.decrement()      
    } catch {
      case ex => println(ex.getMessage())
    }
  }
}
//END:CODE
