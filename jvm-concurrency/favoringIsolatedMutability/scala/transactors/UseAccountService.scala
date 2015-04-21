package com.agiledeveloper.pcj

import akka.actor.Actor
import akka.actor.Actors
import akka.actor.ActorRef
import akka.actor.ActorRegistry

//START:CODE

object UseAccountService {
  
  def printBalance(accountName : String, account : ActorRef) = {
    (account !! FetchBalance) match {
      case Some(Balance(amount)) =>
        println(accountName + " balance is " + amount)
      case None =>
        println("Error getting balance for " + accountName)
    }
  }
  
  def main(args : Array[String]) = {
    val account1 = Actor.actorOf[Account].start()
    val account2 = Actor.actorOf[Account].start()
    val accountService = Actor.actorOf[AccountService].start()
  
    account1 ! Deposit(1000)
    account2 ! Deposit(1000)
    
    Thread.sleep(1000)

    printBalance("Account1", account1)
    printBalance("Account2", account2)
    
    println("Let's transfer $20... should succeed")
    accountService ! Transfer(account1, account2, 20)

    Thread.sleep(1000)

    printBalance("Account1", account1)
    printBalance("Account2", account2)

    println("Let's transfer $2000... should not succeed")
    accountService ! Transfer(account1, account2, 2000)

    Thread.sleep(6000)

    printBalance("Account1", account1)
    printBalance("Account2", account2)
    
    Actors.registry.shutdownAll
  }
}
//END:CODE
