package com.example

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, UUID}

import com.example.RoomBookingService.BookingError
import com.example.RoomBookingService.BookingError.{RoomAlreadyBooked, RoomNotBooked, TokenExpired, UserNotFound}
import scalikejdbc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RoomBookingService(roomBook: RoomBookingDBService, user: UserDataDBLayer) {

  implicit val session: AutoSession.type = AutoSession

  def roomBooking(roomID: String, userId: String, bookingTime: String): Future[Either[BookingError, UserRequestBooking]] = Future {

    if (roomBook.getCount(roomID) == 0) {
      Left(RoomAlreadyBooked)
    }
    else {
      val userData = user.get(userId)
      if (userData.isDefined) {
        val user = userData.get
        val token_exp = user.token_exp
        if (new Timestamp(Calendar.getInstance.getTime.getTime).after(getCalendarTimeStamp(token_exp))) {
          val bid = UUID.randomUUID().toString
          if (roomBook.book(bid, userId, roomID, bookingTime) != 0) {
            val booking = roomBook.getRoom(roomID)
            if (booking.isDefined && booking.get.bid == bid) {
              Right(UserRequestBooking(bookingTime, userId, roomID))
            }
            else {
              Left(RoomNotBooked)
            }
          }
          else {
            Left(RoomAlreadyBooked)
          }
        }
        else {
          Left(TokenExpired)
        }
      }
      else {
        Left(UserNotFound)
      }
    }
  }

  def getCalendarTimeStamp(token_exp: Any): Timestamp = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
    val parsedDate = dateFormat.parse(token_exp.toString)
    new Timestamp(parsedDate.getTime)
  }
}

object RoomBookingService {
  self =>

  sealed trait BookingError

  object BookingError {

    case object MaxTimeBookingError extends BookingError

    case object UserNotFound extends BookingError

    case object RoomAlreadyBooked extends BookingError

    case object TokenExpired extends BookingError

    case object RoomNotBooked extends BookingError

  }

}
