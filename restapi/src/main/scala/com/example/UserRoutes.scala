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
    path("register-user") {
      (post & entity(as[UserRequest])) { userRequest =>
        val userId = userDBService.addUsers(
          userRequest.name,
          userRequest.password,
          userRequest.email
        )
        complete("done")
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
