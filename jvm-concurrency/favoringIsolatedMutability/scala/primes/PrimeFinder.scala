package com.agiledeveloper.pcj

//START:CODE
object PrimeFinder {
  def isPrime(number : Int) : Boolean = {
    if (number <= 1) return false

    var limit = scala.math.sqrt(number).toInt
    var i = 2
    while(i <= limit) {
      if(number % i == 0) return false
      i += 1
    }    
    return true
  }

  def countPrimesInRange(lower : Int, upper : Int) : Int = {    
    var count = 0
    var index = lower
    while(index <= upper) {
      if(isPrime(index)) count += 1
      index += 1
    }    
    count
  }  
}
//END:CODE
