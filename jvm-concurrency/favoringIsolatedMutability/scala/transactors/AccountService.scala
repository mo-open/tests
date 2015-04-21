package com.agiledeveloper.pcj

import akka.transactor.Transactor
import akka.actor.ActorRef

//START:CODE
class AccountService extends Transactor {
  
  override def coordinate = { 
    case Transfer(from, to, amount) =>
      sendTo(to -> Deposit(amount), from -> Withdraw(amount))
  }

  def atomically = { case message => }
}
//END:CODE
