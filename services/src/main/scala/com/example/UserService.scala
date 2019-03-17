package com.example

import java.util.UUID

import com.example.UserService.{SignInError, SignUpError}
import scalikejdbc.AutoSession

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserService(userDB: UserDataDBLayer) {

  implicit val session: AutoSession.type = AutoSession

  def registerUsers(name: String, email: String, password: String): Future[Either[SignUpError, UserResponse]] = Future {
    val uid = UUID.randomUUID().toString
    if (userDB.getCount(email) == 0) {
      if (userDB.create(name, password, email, uid.toString) != 0) {
        Right(UserResponse(uid, name, email))
      }
      else {
        Left(SignUpError.UserNotCreated)
      }
    }
    else {
      Left(SignUpError.EmailAlreadyTaken(email))
    }
  }

  def signInUser(email: String, password: String): Future[Either[SignInError, UserData]] = Future {
    val userData = userDB.get(email, password)
    println(userData)
    userData match {
      case Some(value) => Right(value)
      case None => Left(SignInError.InvalidCredentials)
    }
  }

  def generateToken(uid: String, name: String, email: String): String = {
    UUID.randomUUID().toString + "." + uid + "." + name + "." + email
  }
}

object UserService {
  self =>

  sealed trait SignUpError

  object SignUpError {

    case class EmailAlreadyTaken(email: String) extends SignUpError

    case object UserIdIsNotUnique extends SignUpError

    case object UserNotCreated extends SignUpError

  }

  sealed trait SignInError

  object SignInError {

    case object InvalidCredentials extends SignInError

  }

}
