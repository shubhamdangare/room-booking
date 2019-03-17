package com.example

import play.api.libs.json.{Json, Reads}

case class UserRequest(name: String, password: String, email: String)

object UserRequest {

  implicit val UsesReads: Reads[UserRequest] = Json.reads[UserRequest]

}
