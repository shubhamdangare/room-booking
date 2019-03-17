package com.example

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.example.UserService.SignInError.InvalidCredentials
import com.example.UserService.SignUpError.{EmailAlreadyTaken, UserIdIsNotUnique, UserNotCreated}
import com.example.UserService.{SignInError, SignUpError}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import akka.http.scaladsl.server.Route

class UserRoute(userDBService: UserService) extends PlayJsonSupport {

  val route: Route = {
    get {
      path("sign-in") {
        parameters('name.as[String], 'pass.as[String]) { (name, pass) => {
          onSuccess(userDBService.signInUser(name, pass)) {
            case Right(user) => complete(user.token.toString)
            case Left(error) => complete(translateError(error))
          }
        }
        }
      }
    } ~ path("sign-up") {
      (post & entity(as[UserRequest])) { userRequest =>
        onSuccess(userDBService.registerUsers(
          userRequest.name,
          userRequest.email,
          userRequest.password
        )) {
          case Right(user) => complete(user)
          case Left(error) => complete(translateError(error))
        }
      }
    }
  }

  private def translateError(error: SignUpError): (StatusCodes.ClientError, ErrorResponse) = {
    error match {
      case EmailAlreadyTaken(email) =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue,
          s"User with email $email already registered")
      case UserIdIsNotUnique =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue,
          s"Sorry For Inconvience Try Again")
      case UserNotCreated =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue,
          s"User Not Created")
    }
  }

  private def translateError(signInError: SignInError): (StatusCodes.ClientError, ErrorResponse) = {
    signInError match {
      case InvalidCredentials =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue, "Invalid credentials")
    }
  }

}
