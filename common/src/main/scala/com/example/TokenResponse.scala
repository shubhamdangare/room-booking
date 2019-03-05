package com.example

import play.api.libs.json.{Json, OWrites}

case class TokenResponse(token: String)

object TokenResponse {

  implicit val TokenResponseWrite: OWrites[TokenResponse] = Json.writes[TokenResponse]
}
