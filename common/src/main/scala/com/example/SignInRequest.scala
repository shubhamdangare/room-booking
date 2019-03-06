package com.example

import play.api.libs.json.{ Json, Reads }

case class SignInRequest(name: String, password: String)

object SignInRequest {
  implicit val SignInRequestReads: Reads[SignInRequest] = Json.reads[SignInRequest]
}
