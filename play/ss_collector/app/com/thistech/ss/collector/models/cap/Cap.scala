package com.thistech.ss.collector.models.cap

import scala.xml.{Node, Elem}

object CapValueElement {
    val valueName = "valueName"
    val value = "value"
}

case class CapValue(valueName: String, value: String)

object ResourceElement {
    val resourceDesc = "resourceDesc"
    val mimeType = "mimeType"
    val size = "size"
    val uri = "uri"
    val derefUri = "derefUri"
    val digest = "digest"
}

case class Resource(resourceDesc: String, mimeType: String,
                    size: Int, uri: String, derefUri: String, digest: String)

object AreaElement {
    val areaDesc = "areaDesc"
    val polygon = "polygon"
    val circle = "circle"
    val geocode = "geocode"
    val altitude = "altitude"
    val ceiling = "ceiling"
}

case class Area(areaDesc: String, polygon: Seq[CapValue], circle: Seq[CapValue],
                geocode: Seq[CapValue], altitude: Float, ceiling: Float)

object InfoElement {
    val language = "language"
    val category = "category"
    val event = "event"
    val responseType = "responseType"
    val urgency = "urgency"
    val severity = "severity"
    val certainty = "certainty"
    val audience = "audience"
    val eventCode = "eventCode"
    val effective = "effective"
    val onSet = "onSet"
    val expires = "expires"
    val senderName = "senderName"
    val headLine = "headLine"
    val description = "description"
    val instruction = "instruction"
    val web = "web"
    val contact = "contact"
    val parameter = "parameter"
    val resource = "resource"
    val area = "area"
}

case class Info(language: String = "en-US", category: String, event: String,
                responseType: String, urgency: String, severity: String,
                certainty: String, audience: String, eventCode: CapValue,
                effective: String, onSet: String, expires: String, senderName: String,
                headLine: String, description: String, instruction: String,
                web: String, contact: String, parameter: Seq[CapValue], resource: Seq[Resource],
                area: Seq[Area])

object AlertElement {
    val identity = "identity"
    val sender = "sender"
    val dateTime = "dateTime"
    val status = "status"
    val msgType = "msgType"
    val sources = "sources"
    val scope = "scope"
    val restrictions = "restrictions"
    val addresses = "addresses"
    val code = "code"
    val note = "note"
    val references = "references"
    val incidents = "incidents"
    val info = "info"
}

case class Alert(identity: String, sender: String, dateTime: String, status: String,
                 msgType: String, sources: String, scope: String, restrictions: String,
                 addresses: String, code: String, note: String, references: String,
                 incidents: String, info: Seq[Info])

object CapParser {
    def alert(el: Node): Alert = {
        Alert((el \ AlertElement.identity).text,
            (el \ AlertElement.sender).text,
            (el \ AlertElement.dateTime).text,
            (el \ AlertElement.status).text,
            (el \ AlertElement.msgType).text,
            (el \ AlertElement.sources).text,
            (el \ AlertElement.scope).text,
            (el \ AlertElement.restrictions).text,
            (el \ AlertElement.addresses).text,
            (el \ AlertElement.code).text,
            (el \ AlertElement.note).text,
            (el \ AlertElement.references).text,
            (el \ AlertElement.incidents).text,
            (el \ AlertElement.info).map { n => info(n)}.toList
        )
    }

    def capValue(el: Node): CapValue = {
        if (el != null)
            CapValue((el \ CapValueElement.valueName).text, (el \ CapValueElement.value).text)
        null
    }

    def resource(el: Node): Resource = {
        Resource(
            (el \ ResourceElement.resourceDesc).text,
            (el \ ResourceElement.mimeType).text,
            (el \ ResourceElement.size).text.toInt,
            (el \ ResourceElement.uri).text,
            (el \ ResourceElement.derefUri).text,
            (el \ ResourceElement.digest).text
        )
    }

    def area(el: Node): Area = {
        Area(
            (el \ AreaElement.areaDesc).text,
            (el \ AreaElement.polygon).map { n => capValue(n)}.toSeq,
            (el \ AreaElement.circle).map { n => capValue(n)}.toSeq,
            (el \ AreaElement.geocode).map { n => capValue(n)}.toSeq,
            (el \ AreaElement.altitude).text.toFloat,
            (el \ AreaElement.ceiling).text.toFloat
        )
    }

    def info(el: Node): Info = {
        Info(
            (el \ InfoElement.language).text,
            (el \ InfoElement.category).text,
            (el \ InfoElement.event).text,
            (el \ InfoElement.responseType).text,
            (el \ InfoElement.urgency).text,
            (el \ InfoElement.severity).text,
            (el \ InfoElement.certainty).text,
            (el \ InfoElement.audience).text,
            capValue((el \ InfoElement.eventCode).headOption.orNull),
            (el \ InfoElement.effective).text,
            (el \ InfoElement.onSet).text,
            (el \ InfoElement.expires).text,
            (el \ InfoElement.senderName).text,
            (el \ InfoElement.headLine).text,
            (el \ InfoElement.description).text,
            (el \ InfoElement.instruction).text,
            (el \ InfoElement.web).text,
            (el \ InfoElement.contact).text,
            (el \ InfoElement.parameter).map { p => capValue(p)}.toSeq,
            (el \ InfoElement.resource).map { n => resource(n)}.toSeq,
            (el \ InfoElement.area).map { n => area(n)}.toSeq
        )
    }
}

object JsonImplicit {

    import play.api.libs.json._
    import play.api.libs.json.Reads._
    import play.api.libs.functional.syntax._

    implicit val areaWrites: Writes[Area] = (
        (JsPath \ AreaElement.areaDesc).write[String] and
            (JsPath \ AreaElement.polygon).write[Seq[CapValue]] and
            (JsPath \ AreaElement.circle).write[Seq[CapValue]] and
            (JsPath \ AreaElement.geocode).write[Seq[CapValue]] and
            (JsPath \ AreaElement.altitude).write[Float] and
            (JsPath \ AreaElement.ceiling).write[Float]
        )(unlift(Area.unapply))

    implicit val areaReads: Reads[Area] = (
        (JsPath \ AreaElement.areaDesc).read[String] and
            (JsPath \ AreaElement.polygon).read[Seq[CapValue]] and
            (JsPath \ AreaElement.circle).read[Seq[CapValue]] and
            (JsPath \ AreaElement.geocode).read[Seq[CapValue]] and
            (JsPath \ AreaElement.altitude).read[Float] and
            (JsPath \ AreaElement.ceiling).read[Float]
        )(Area.apply _)

    implicit val alertReads: Reads[Alert] = (
        (JsPath \ AlertElement.identity).read[String] and
            (JsPath \ AlertElement.sender).read[String] and
            (JsPath \ AlertElement.dateTime).read[String] and
            (JsPath \ AlertElement.status).read[String] and
            (JsPath \ AlertElement.msgType).read[String] and
            (JsPath \ AlertElement.sources).read[String] and
            (JsPath \ AlertElement.scope).read[String] and
            (JsPath \ AlertElement.restrictions).read[String] and
            (JsPath \ AlertElement.addresses).read[String] and
            (JsPath \ AlertElement.code).read[String] and
            (JsPath \ AlertElement.note).read[String] and
            (JsPath \ AlertElement.references).read[String] and
            (JsPath \ AlertElement.incidents).read[String] and
            (JsPath \ AlertElement.info).read[Seq[Info]]
        )(Alert.apply _)

    implicit val alertWrites: Writes[Alert] = (
        (JsPath \ AlertElement.identity).write[String] and
            (JsPath \ AlertElement.sender).write[String] and
            (JsPath \ AlertElement.dateTime).write[String] and
            (JsPath \ AlertElement.status).write[String] and
            (JsPath \ AlertElement.msgType).write[String] and
            (JsPath \ AlertElement.sources).write[String] and
            (JsPath \ AlertElement.scope).write[String] and
            (JsPath \ AlertElement.restrictions).write[String] and
            (JsPath \ AlertElement.addresses).write[String] and
            (JsPath \ AlertElement.code).write[String] and
            (JsPath \ AlertElement.note).write[String] and
            (JsPath \ AlertElement.references).write[String] and
            (JsPath \ AlertElement.incidents).write[String] and
            (JsPath \ AlertElement.info).write[Seq[Info]]
        )(unlift(Alert.unapply))

    implicit val capValueWrites: Writes[CapValue] = (
        (JsPath \ CapValueElement.valueName).write[String] and
            (JsPath \ CapValueElement.value).write[String]
        )(unlift(CapValue.unapply))

    implicit val capValueReads: Reads[CapValue] = (
        (JsPath \ CapValueElement.valueName).read[String] and
            (JsPath \ CapValueElement.value).read[String]
        )(CapValue.apply _)

    implicit val infoWrites: Writes[Info] = (
        (JsPath \ InfoElement.language).write[String] and
            (JsPath \ InfoElement.category).write[String] and
            (JsPath \ InfoElement.event).write[String] and
            (JsPath \ InfoElement.responseType).write[String] and
            (JsPath \ InfoElement.urgency).write[String] and
            (JsPath \ InfoElement.severity).write[String] and
            (JsPath \ InfoElement.certainty).write[String] and
            (JsPath \ InfoElement.audience).write[String] and
            (JsPath \ InfoElement.eventCode).write[CapValue] and
            (JsPath \ InfoElement.effective).write[String] and
            (JsPath \ InfoElement.onSet).write[String] and
            (JsPath \ InfoElement.expires).write[String] and
            (JsPath \ InfoElement.senderName).write[String] and
            (JsPath \ InfoElement.headLine).write[String] and
            (JsPath \ InfoElement.description).write[String] and
            (JsPath \ InfoElement.instruction).write[String] and
            (JsPath \ InfoElement.web).write[String] and
            (JsPath \ InfoElement.contact).write[String] and
            (JsPath \ InfoElement.parameter).write[Seq[CapValue]] and
            (JsPath \ InfoElement.resource).write[Seq[Resource]] and
            (JsPath \ InfoElement.area).write[Seq[Area]]
        )(unlift(Info.unapply))

    implicit val infoReads: Reads[Info] = (
        (JsPath \ InfoElement.language).read[String] and
            (JsPath \ InfoElement.category).read[String] and
            (JsPath \ InfoElement.event).read[String] and
            (JsPath \ InfoElement.responseType).read[String] and
            (JsPath \ InfoElement.urgency).read[String] and
            (JsPath \ InfoElement.severity).read[String] and
            (JsPath \ InfoElement.certainty).read[String] and
            (JsPath \ InfoElement.audience).read[String] and
            (JsPath \ InfoElement.eventCode).read[CapValue] and
            (JsPath \ InfoElement.effective).read[String] and
            (JsPath \ InfoElement.onSet).read[String] and
            (JsPath \ InfoElement.expires).read[String] and
            (JsPath \ InfoElement.senderName).read[String] and
            (JsPath \ InfoElement.headLine).read[String] and
            (JsPath \ InfoElement.description).read[String] and
            (JsPath \ InfoElement.instruction).read[String] and
            (JsPath \ InfoElement.web).read[String] and
            (JsPath \ InfoElement.contact).read[String] and
            (JsPath \ InfoElement.parameter).read[Seq[CapValue]] and
            (JsPath \ InfoElement.resource).read[Seq[Resource]] and
            (JsPath \ InfoElement.area).read[Seq[Area]]
        )(Info.apply _)

    implicit val resourceWrites: Writes[Resource] = (
        (JsPath \ ResourceElement.resourceDesc).write[String] and
            (JsPath \ ResourceElement.mimeType).write[String] and
            (JsPath \ ResourceElement.size).write[Int] and
            (JsPath \ ResourceElement.uri).write[String] and
            (JsPath \ ResourceElement.derefUri).write[String] and
            (JsPath \ ResourceElement.digest).write[String]
        )(unlift(Resource.unapply))

    implicit val resourceReads: Reads[Resource] = (
        (JsPath \ ResourceElement.resourceDesc).read[String] and
            (JsPath \ ResourceElement.mimeType).read[String] and
            (JsPath \ ResourceElement.size).read[Int] and
            (JsPath \ ResourceElement.uri).read[String] and
            (JsPath \ ResourceElement.derefUri).read[String] and
            (JsPath \ ResourceElement.digest).read[String]
        )(Resource.apply _)

}
