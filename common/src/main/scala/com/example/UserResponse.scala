package com.example


import play.api.libs.json.{Json, OWrites}

case class UserResponse(uid: String, name: String, email: String)

object UserResponse {

  implicit val UsesWrites: OWrites[UserResponse] = Json.writes[UserResponse]

  def toDomain(userResponse:UserData):UserResponse =
    UserResponse(userResponse.uid, userResponse.name, userResponse.email)
}

