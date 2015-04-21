package com.agiledeveloper.pcj

import akka.actor.TypedActor
import akka.transactor.Coordination.coordinate

//START:CODE
class AccountServiceImpl extends TypedActor with AccountService {
  def transfer(from : Account, to : Account, amount : Int) = {
    coordinate {
      to.deposit(amount)
      from.withdraw(amount)
    }
  }
}
//END:CODE
