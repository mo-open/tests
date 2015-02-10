package org.kafecho.learning.jozispray

import spray.testkit.ScalatestRouteTest
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import spray.routing.HttpService

@RunWith(classOf[JUnitRunner])
class CalculatorHttpServiceSpec extends WordSpec with ShouldMatchers with ScalatestRouteTest with HttpService {
  def actorRefFactory = system
  val api = pathPrefix("api"){
    pathPrefix("calculator"){
      path("plus" / IntNumber / IntNumber){ (a,b) =>
        get{
          complete( (a+b).toString)
        }
      }
    }
  }
  
  "The calculator" should {
    "Correctly add numbers" in {
      val a = 10
      val b = 398
      Get(s"/api/calculator/plus/$a/$b") ~> api ~> check{
        (responseAs[String]).toInt === (a + b)
      }
    }
  }
}
