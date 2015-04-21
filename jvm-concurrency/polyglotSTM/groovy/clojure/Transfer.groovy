/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
import clojure.lang.Ref
import clojure.lang.LockingTransaction
import java.util.concurrent.Callable

class Account {
  final private Ref currentBalance

  public Account(initialBalance) {
    currentBalance = new Ref(initialBalance)
  }

  def getBalance() { currentBalance.deref() }

  def deposit(amount) {
    LockingTransaction.runInTransaction({
        if(amount > 0) {
          currentBalance.set(currentBalance.deref() + amount)
          println "deposit ${amount}... will it stay"
        } else {
          throw new RuntimeException("Operation invalid")
        }
    } as Callable)
  }
  
  def withdraw(amount) {
    LockingTransaction.runInTransaction({
      if(amount > 0 && currentBalance.deref() >= amount)
        currentBalance.set(currentBalance.deref() - amount)
      else 
        throw new RuntimeException("Operation invalid")
    } as Callable)
  }
}

def transfer(from, to, amount) {
  LockingTransaction.runInTransaction({
    to.deposit(amount)
    from.withdraw(amount)
  } as Callable)
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
