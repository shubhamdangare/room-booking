package com.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.io.StdIn

object UserRoutes extends App with PlayJsonSupport {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val userDBService = new UserDataDBService

  val route = {
    get {
      path("sign-in") {
        parameters('name.as[String], 'pass.as[String]) { (name, pass) => {
          val tokenId = userDBService.signInUser(name, pass).getOrElse("Invalid UserName or Password")
          complete(TokenResponse(tokenId))
        }
        }
      }
    } ~ path("register-user") {
      (post & entity(as[UserRequest])) { userRequest =>
        val tokenId = userDBService.registerUsers(
          userRequest.name,
          userRequest.password,
          userRequest.email
        )
        complete(TokenResponse(tokenId))
      }
    } ~ path("booking") {
      (post & entity(as[UserRequestBooking])) { userBooking =>

        val bookingResponse = userDBService.roomBooking(userBooking.bookingTime, userBooking.uid, userBooking.rid)
        complete(TokenResponse(bookingResponse))
      }
    }
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8000)
  println(s"Server online at http://localhost:8000/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
