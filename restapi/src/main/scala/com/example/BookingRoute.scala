package com.example

import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.example.RoomBookingService.BookingError
import com.example.RoomBookingService.BookingError._

class BookingRoute(booking: RoomBookingService) extends PlayJsonSupport {


  val route: Route = {
    get {
      path("book") {
        parameters('bookTime.as[String], 'uid.as[String], 'rid.as[String]) { (time, uid, rid) => {
          onSuccess(booking.roomBooking(rid, uid, time)) {
            case Right(user) => complete(user)
            case Left(error) => complete(translateError(error))
          }
        }
        }
      }
    }
  }

  private def translateError(error: BookingError): (StatusCodes.ClientError, ErrorResponse) = {
    error match {
      case MaxTimeBookingError =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue,
          s"Cannot Book for that long")
      case UserNotFound =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue,
          s"User Not Active anyMore")
      case RoomAlreadyBooked =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue,
          s"Room already Booked")
      case TokenExpired =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue,
          s"Token Expired")
      case RoomNotBooked =>
        StatusCodes.BadRequest -> ErrorResponse(StatusCodes.BadRequest.intValue,
          s"Room not Boooked")
    }
  }
}
