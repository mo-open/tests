package com.agiledeveloper.pcj

import akka.actor.TypedActor
import akka.actor.ActorRegistry
import akka.actor.Actors

//START:CODE
object UseAccountService {
  
  def main(args : Array[String]) = {
    val account1 = 
      TypedActor.newInstance(classOf[Account], classOf[AccountImpl])
    val account2 = 
      TypedActor.newInstance(classOf[Account], classOf[AccountImpl])
    val accountService = 
      TypedActor.newInstance(
        classOf[AccountService], classOf[AccountServiceImpl])

    account1.deposit(1000)
    account2.deposit(1000)
    
    println("Account1 balance is " + account1.getBalance())
    println("Account2 balance is " + account2.getBalance())


    println("Let's transfer $20... should succeed")

    accountService.transfer(account1, account2, 20)

    Thread.sleep(1000)

    println("Account1 balance is " + account1.getBalance())
    println("Account2 balance is " + account2.getBalance())

    println("Let's transfer $2000... should not succeed")
    accountService.transfer(account1, account2, 2000)

    Thread.sleep(6000)

    println("Account1 balance is " + account1.getBalance())
    println("Account2 balance is " + account2.getBalance())
    
    Actors.registry.shutdownAll
  }
}
//END:CODE
