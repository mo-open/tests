package org.akka.essentials.stm.transactor.example
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import scala.concurrent.duration._
import java.lang.Float

case class AccountBalance(accountNumber: String, accountBalance: Float)
case class AccountCredit(amount: Float)
case class AccountDebit(amount: Float)
case class TransferMsg(amtToBeTransferred: Float)

object BankApplication {
  val system = ActorSystem("STM-Example")
  implicit val timeout = Timeout(5 seconds)
  val bank = system.actorOf(Props[BankActor], name = "BankActor")

  def main(args: Array[String]): Unit = {

    showBalances
    bank ! new TransferMsg(1500f)
    showBalances
    bank ! new TransferMsg(1400f)
    showBalances
    bank ! new TransferMsg(3500f)
    showBalances

    system.shutdown
  }
  def showBalances(): Unit = {
    Thread.sleep(2000)
    bank ! new AccountBalance("XYZ", 0f)
    bank ! new AccountBalance("ABC", 0f)
  }

}