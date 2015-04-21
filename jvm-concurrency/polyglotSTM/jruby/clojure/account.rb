#---
# Excerpted from "Programming Concurrency on the JVM",
# published by The Pragmatic Bookshelf.
# Copyrights apply to this code. It may not be used to create training material, 
# courses, books, articles, and the like. Contact us if you are in doubt.
# We make no guarantees that this code is fit for any purpose. 
# Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
#---
require 'java'
java_import 'clojure.lang.Ref'
java_import 'clojure.lang.LockingTransaction'

class Account
  def initialize(initialBalance)
    @balance = Ref.new(initialBalance)
  end
  
  def balance
    @balance.deref
  end  
  
  def deposit(amount)
    LockingTransaction.run_in_transaction do
      if amount > 0
        @balance.set(@balance.deref + amount)
        puts "deposited $#{amount}... will it stay"
      else
        raise "Operation invalid"
      end
    end
  end

  def withdraw(amount)
    LockingTransaction.run_in_transaction do
      if amount > 0 && @balance.deref >= amount
        @balance.set(@balance.deref - amount)
      else
        raise "Operation invalid"
      end
    end
  end
end

def transfer(from, to, amount)
  LockingTransaction.run_in_transaction do
    to.deposit(amount)
    from.withdraw(amount)
  end
end

def transfer_and_print(from, to, amount)
  begin
    transfer(from, to, amount)
  rescue => ex
    puts "transfer failed #{ex}"
  end

  puts "Balance of from account is #{from.balance}"
  puts "Balance of to account is #{to.balance}"
end

account1 = Account.new(2000)
account2 = Account.new(100)

transfer_and_print(account1, account2, 500)
transfer_and_print(account1, account2, 5000)
