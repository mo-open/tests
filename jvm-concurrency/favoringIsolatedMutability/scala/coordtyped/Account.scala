package com.agiledeveloper.pcj

import akka.transactor.annotation.Coordinated

//START:CODE
trait Account {
  def getBalance() : Int
  @Coordinated def deposit(amount : Int) : Unit
  @Coordinated def withdraw(amount : Int) : Unit
}
//END:CODE
