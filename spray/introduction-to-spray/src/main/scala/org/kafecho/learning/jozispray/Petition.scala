package org.kafecho.learning.jozispray

import scala.concurrent.duration.DurationInt

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
import spray.httpx.marshalling.ToResponseMarshallable.isMarshallable
import spray.json.DefaultJsonProtocol
import spray.routing.Directive.pimpApply
import spray.routing.SimpleRoutingApp

case class Person(firstname: String, surname: String, age: Int, city: String)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val PersonFormat = jsonFormat4(Person)
}

object SignatureManager{
  case class AddSignatory(person: Person)
  case object GetSignatories
}

class SignaturesManager extends Actor{
  import SignatureManager._
  var signatories : List[Person] = Nil
  def receive = {
    case AddSignatory(p) => signatories = p :: signatories
    case GetSignatories => sender ! signatories
  }
}

object Petition extends SimpleRoutingApp with App{
  import SignatureManager._
  implicit val system = ActorSystem("test")
  //implicit val ec = system.scheduler
  val signaturesManager = system.actorOf(Props[SignaturesManager])
  import MyJsonProtocol._
  implicit def executionContext = actorRefFactory.dispatcher
  implicit val askTimeout = Timeout(2 seconds)
  
  startServer("localhost", 8080){
    pathPrefix("api"){
      path("signatures"){
        get{
          complete{
            (signaturesManager ? GetSignatories).mapTo[List[Person]]
          }
        } ~ 
        post{
            entity(as[Person]){p =>
            	complete{
            	  signaturesManager ! AddSignatory(p)
            	  s"Thanks for signing up ${p.firstname}!"
            	}
            }
        }
      }
    }
  }
}