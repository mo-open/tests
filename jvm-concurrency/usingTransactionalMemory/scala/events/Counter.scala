package com.agiledeveloper.pcj

import akka.stm.{Ref, atomic, deferred, compensating}

//START:CODE
class Counter {
  private val value = Ref(1)
      
  def decrement() = {
    atomic {      
      
      deferred { println("Transaction completed...send email, log, etc.") }
      
      compensating { println("Transaction aborted...hold the phone") }
      
      if(value.get() <= 0) 
        throw new RuntimeException("Operation not allowed")
                         
      value.swap(value.get() - 1)
      value.get()              
    }
  }
}
//END:CODE
