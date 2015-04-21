import clojure.lang.Ref
import clojure.lang.LockingTransaction
import java.util.concurrent.Callable

//START:REFS
class Account(val initialBalance : Int) {
  val balance = new Ref(initialBalance)
  
  def getBalance() = balance.deref
//END:REFS

//START:DEPOSIT  
  def deposit(amount : Int) = {
    LockingTransaction runInTransaction new Callable[Boolean] {
      def call() = {
        if(amount > 0) {
          val currentBalance = balance.deref.asInstanceOf[Int]
          balance.set(currentBalance + amount)
          println("deposit " + amount + "... will it stay")
          true
        } else throw new RuntimeException("Operation invalid")
      }
    }
  }
//END:DEPOSIT  

//START:WITHDRAW
  def withdraw(amount : Int) = {
    LockingTransaction runInTransaction new Callable[Boolean] {
      def call() = {
        val currentBalance = balance.deref.asInstanceOf[Int]
        if(amount > 0 && currentBalance >= amount) {
          balance.set(currentBalance - amount)
          true
        } else throw new RuntimeException("Operation invalid")
      }
    }
  }
}
//END:WITHDRAW

//START:TRANSFER
def transfer(from : Account, to : Account, amount : Int) = {
  LockingTransaction runInTransaction new Callable[Boolean] {
    def call() = {
      to.deposit(amount)
      from.withdraw(amount)
      true      
    }
  }
}
//END:TRANSFER

//START:USE
def transferAndPrint(from : Account, to : Account, amount : Int) = {
  try {
    transfer(from, to, amount)
  } catch {
    case ex => println("transfer failed " + ex)
  }

  println("Balance of from account is " + from.getBalance())
  println("Balance of to account is " + to.getBalance())
}

val account1 = new Account(2000)
val account2 = new Account(100)

transferAndPrint(account1, account2, 500)
transferAndPrint(account1, account2, 5000)
//END:USE
