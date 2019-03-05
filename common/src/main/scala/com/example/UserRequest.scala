package com.example

case class UserRequest(name: String, password: String, email: String)

import play.api.libs.json.{Json, Reads}

object UserRequest {

  implicit val UsesReads: Reads[UserRequest] = Json.reads[UserRequest]

}