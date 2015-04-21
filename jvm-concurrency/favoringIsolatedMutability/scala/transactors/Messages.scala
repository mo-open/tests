package com.agiledeveloper.pcj

import akka.actor.ActorRef

//START:CODE
case class Deposit(val amount : Int)

case class Withdraw(val amount : Int)

case class FetchBalance()

case class Balance(val amount : Int)

case  class Transfer(val from : ActorRef, val to : ActorRef, val amount : Int)
//END:CODE
