package com.agiledeveloper.pcj

//START:CODE
trait AccountService {
  def transfer(from : Account, to : Account, amount : Int) : Unit
}
//END:CODE
