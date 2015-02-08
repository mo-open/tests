package com.thistech.ss.collector.models.cap

import play.api.libs.json.Json

/**
 *
 * @author Dongsong
 */

object Status extends Enumeration {
    type Status = Value
    val Actual, Exercise, System, Test, Draft = Value
}

object MsgType extends Enumeration {
    type MsgType = Value
    val Alert, Update, Cancel, Ack, Error = Value
}

object Scope extends Enumeration {
    type Scope = Value
    val Public, Restricted, Private = Value
}

object Category extends Enumeration {
    type Category = Value
    val Geo, Met, Safety, Security, Rescue, Fire, Health, Env, Transport, Infra, CBRNE, Other = Value
}

object ResponseType extends Enumeration {
    type ResponseType = Value
    val Shelter, Evacuate, Prepare, Execute, Avoid, Monitor, Assess, AllClear, None = Value
}

object Urgency extends Enumeration {
    type Urgency = Value
    val Immediate, Expected, Future, Past, Unknown = Value
}

object Severity extends Enumeration {
    type Severity = Value
    val Extreme, Severe, Moderate, Minor, Unknown = Value
}

object Certainty extends Enumeration {
    type Certainty = Value
    val Observed, Likely, Possible, Unlikely, Unknown = Value
}
