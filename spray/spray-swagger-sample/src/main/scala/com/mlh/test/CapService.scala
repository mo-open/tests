package com.mlh.test

import com.gettyimages.spray.swagger.Person
import com.mlh.sprayswaggersample.Json4sSupport
import javax.ws.rs.Path

import com.wordnik.swagger.annotations._
import spray.http.StatusCodes._
import spray.routing.HttpService

@Api(value = "/cap", description = "Operations about people.", produces = "application/json", position = 2)
trait CapService extends HttpService {

    import com.mlh.sprayswaggersample.Json4sSupport._

    val routes = postCap

    @ApiOperation(value = "Post a person", notes = "", nickname = "postPerson", httpMethod = "POST")
    @ApiImplicitParams(Array(
        new ApiImplicitParam(name = "body", value = "Person with name", dataType = "Alert2", required = true, paramType = "body")
    ))
    @ApiResponses(Array(
        new ApiResponse(code = 200, message = "Person got created"),
        new ApiResponse(code = 500, message = "Internal server error"),
        new ApiResponse(code = 405, message = "Invalid input")
    ))
    def postCap =
        path("person") {
            post {
                entity(as[Alert2]) {
                    person => complete(person)
                }
            }
        }

    @ApiOperation(value = "IGNORE", notes = "", hidden = true, httpMethod = "GET", response = classOf[Alert2])
    protected def showAlert2 = Unit
}

case class Alert2(message: String)
