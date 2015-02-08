package com.thistech.ss.collector

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

    "Application" should {

        "send 404 on a bad request" in new WithApplication {
            route(FakeRequest(GET, "/boum")) must beNone
        }

        "render the index page" in new WithApplication {
            val request = route(FakeRequest(POST, "/xml").withXmlBody(<alert></alert>)).post("<Alert/>")

            status(request.value.get) must equalTo(OK)
        }
    }
}
