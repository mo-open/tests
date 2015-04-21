package com.agiledeveloper.pcj

import akka.transactor.Transactor
import akka.stm.Ref

//START:CODE
class Account extends Transactor {
  val balance = Ref(0)
  
  def atomically = {
    case Deposit(amount) =>
      if (amount > 0) {
        balance.swap(balance.get() + amount)
        println("Received Deposit request " + amount)
      }

    case Withdraw(amount) =>
      println("Received Withdraw request " + amount)
      if (amount > 0 && balance.get() >= amount) 
        balance.swap(balance.get() - amount)
      else {
        println("...insufficient funds...")
        throw new RuntimeException("Insufficient fund")
      }
 
    case FetchBalance =>
      self.replySafe(Balance(balance.get()))
  }
}
//END:CODE
