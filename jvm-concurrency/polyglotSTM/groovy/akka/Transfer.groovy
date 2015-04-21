/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
import akka.stm.Ref

class Account {
  private final Ref currentBalance
  private final stmPackage = Class.forName('akka.stm.package')
  public Account(initialBalance) { currentBalance = new Ref(initialBalance) }
  def getBalance() { currentBalance.get() }
  def deposit(amount) {
    stmPackage.atomic({
      if(amount > 0) {
        currentBalance.set(currentBalance.get() + amount)
        println "deposit ${amount}... will it stay"
      }      
    } as scala.Function0, stmPackage.DefaultTransactionFactory())
  }  
  def withdraw(amount) {
    stmPackage.atomic({
      if(amount > 0 && currentBalance.get() >= amount)
        currentBalance.set(currentBalance.get() - amount)
      else 
        throw new RuntimeException("Operation invalid")
        } as scala.Function0, stmPackage.DefaultTransactionFactory())
  }
}

def transfer(from, to, amount) {
  def stmPackage = Class.forName('akka.stm.package')
  stmPackage.atomic({
    to.deposit(amount)
    from.withdraw(amount)
    } as scala.Function0, stmPackage.DefaultTransactionFactory())
}

def transferAndPrint(from, to, amount) {
  try {
    transfer(from, to, amount)
  } catch(Exception ex) { 
    println "transfer failed $ex"
  }

  println "Balance of from account is $from.balance"
  println "Balance of to account is  $to.balance"
}

def account1 = new Account(2000)
def account2 = new Account(100)

transferAndPrint(account1, account2, 500)
transferAndPrint(account1, account2, 5000)
