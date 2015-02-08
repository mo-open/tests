package com.thistech.ss.collector.auth

import play.api.mvc._
import scala.concurrent.Future
import play.api.Routes._

object AuthFilter {
    def apply() = new AuthFilter()
}

class AuthFilter() extends Filter {
    def apply(next: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
        if (authorizationRequired(request)) {
            next(request)
        } else next(request)
    }

    private def authorizationRequired(request: RequestHeader) = {
        val actionInvoked: String = request.tags.getOrElse(ROUTE_ACTION_METHOD, "")
        //auth
        true
    }
}
