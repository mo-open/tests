package com.agiledeveloper.pcj

import akka.stm.Ref
import akka.stm.atomic

//START:CODE
class Account(val initialBalance : Int) {
  val balance = Ref(initialBalance)

  def getBalance() = balance.get()
  
  def deposit(amount : Int) = {
    atomic {
      println("Deposit " + amount)
      if(amount > 0) 
        balance.swap(balance.get() + amount)
      else
        throw new AccountOperationFailedException()
    }
  }
  
  def withdraw(amount : Int) = {
    atomic {
      val currentBalance = balance.get()
      if(amount > 0 && currentBalance >= amount) 
        balance.swap(currentBalance - amount)
      else
        throw new AccountOperationFailedException()
    }
  }
}
//END:CODE
