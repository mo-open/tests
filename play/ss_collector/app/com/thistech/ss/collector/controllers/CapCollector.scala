package com.thistech.ss.collector.controllers

import com.thistech.ss.collector.models.cap._
import play.api.mvc._
import play.api._

object CapCollector extends Controller {

    import com.thistech.ss.collector.models.cap.JsonImplicit._

    def publishCapJson = Action(BodyParsers.parse.json) { request =>
        Logger.debug("JSON post")
        val postAlert = request.body.validate[Alert]

        postAlert.fold(
            errors => {
                NotAcceptable("Invalid Alert Message:" + request.body)
            },
            alert => {
                Ok
            }
        )
    }

    def publishCapXml = Action(BodyParsers.parse.xml) { request =>
        Logger.debug("XML post")
        try {
            (request.body \ "Alert").map {
                n => CapParser.alert(n)
            }
        } catch {
            case ex: Exception => Logger.error("", ex)
        }
        Ok
    }
}