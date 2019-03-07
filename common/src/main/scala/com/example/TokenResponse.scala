package com.example

import play.api.libs.json.{Json, OWrites}

case class TokenResponse(response: String)

object TokenResponse {

  implicit val TokenResponseWrite: OWrites[TokenResponse] = Json.writes[TokenResponse]
}
