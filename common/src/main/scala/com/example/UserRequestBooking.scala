package com.example

import play.api.libs.json.{Json, Reads}

case class UserRequestBooking(bookingTime:String, uid: Int, rid: Int)

object UserRequestBooking {
  implicit val UsesReads: Reads[UserRequestBooking] = Json.reads[UserRequestBooking]

}