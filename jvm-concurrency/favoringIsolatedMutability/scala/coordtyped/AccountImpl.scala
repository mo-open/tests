package com.agiledeveloper.pcj

import akka.actor.TypedActor
import akka.stm.Ref

//START:CODE
class AccountImpl extends TypedActor with Account {
  val balance = Ref(0)

  def getBalance() = balance.get()
  
  def deposit(amount : Int) = {
    if (amount > 0) {
      balance.swap(balance.get() + amount)
      println("Received Deposit request " + amount)
    }
  }

  def withdraw(amount : Int) = {
    println("Received Withdraw request " + amount)
    if (amount > 0 && balance.get() >= amount) 
      balance.swap(balance.get() - amount)
    else {
      println("...insufficient funds...")
      throw new RuntimeException("Insufficient fund")
    }
  }
}
//END:CODE
