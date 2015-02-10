package org.kafecho.learning.jozispray

import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object HelloRouting extends SimpleRoutingApp with App{
  implicit val system = ActorSystem("restafarian")
  import system.dispatcher
  
  // In the future, we might all speak mandarin.
  def futureAnswer: Future[String] = Future{
    Thread.sleep(1000)
    "你好!"
  }

  startServer(interface= "localhost", port= 8080){
    pathPrefix("api"){
	    path("hello-now"){
	      get{
	        complete( "Hello, World" )
	      }
	    } ~
	    path("hello-future"){
	      get{
	        complete( futureAnswer )
	      }
	    }
	  }
  }
}