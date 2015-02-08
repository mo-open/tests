import play.api._
import play.api.mvc.RequestHeader
import play.api.mvc.Results._

import scala.concurrent.Future
import com.thistech.ss.collector.auth._
import play.api.mvc._

object Global extends WithFilters(AuthFilter()) {

    override def onStart(app: Application) = {
        Logger.debug("Starting SS Collector ...")
    }

    override def onHandlerNotFound(request: RequestHeader) = {
        Future.successful(NotFound)
    }

    override def onBadRequest(request: RequestHeader, error: String) = {
        Future.successful(BadRequest("Bad Request: " + error))
    }

    override def onError(request: RequestHeader, ex: Throwable) = {
        Logger.error("Error:", ex)
        Future.successful(InternalServerError("" + ex))
    }
}
