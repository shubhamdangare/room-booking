package com.example

import play.api.libs.json.{Json, OWrites}

case class UserRequestBooking(bookingTime: String, uid: String, rid: String)

object UserRequestBooking {
  implicit val UsesReads: OWrites[UserRequestBooking] = Json.writes[UserRequestBooking]

}