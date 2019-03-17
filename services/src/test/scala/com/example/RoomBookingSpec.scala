package com.example

import java.sql.Timestamp
import java.util.Calendar

import com.example.RoomBookingService.BookingError.RoomAlreadyBooked
import org.mockito.Mockito.when
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar

class RoomBookingSpec extends WordSpec with ScalaFutures with Matchers with MockitoSugar with BeforeAndAfter {

  val mockedBookingdDB = mock[RoomBookingDBService]
  val userMocked = mock[UserDataDBLayer]
  val booking = new RoomBookingService(mockedBookingdDB, userMocked)
  val calendar = Calendar.getInstance
  val token_exp = new Timestamp(calendar.getTime.getTime)

  "bookingServices" should {
    "room already Booked" in {
      when(mockedBookingdDB.getCount("asdasda")).thenReturn(1.toLong)
      whenReady(booking.roomBooking("Asd", "Asd", "asd")) {
        response => response shouldBe Left(RoomAlreadyBooked)
      }
    }

    "room already Booked" in {
      when(userMocked.get("asdasda", "asdas")).thenReturn(Option(new UserData(
        "sadas",
        "sdasdas",
        "asdasda",
        "ASDasdas",
        "asdasdasd",
        token_exp)))

      when(mockedBookingdDB.getCount("asdasda")).thenReturn(1.toLong)
      whenReady(booking.roomBooking("Asd", "Asd", "asd")) {
        response => response shouldBe Left(RoomAlreadyBooked)
      }
    }
  }

}
