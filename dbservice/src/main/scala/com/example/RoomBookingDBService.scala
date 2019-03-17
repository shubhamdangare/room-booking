package com.example

import scalikejdbc._
import scalikejdbc.interpolation.SQLSyntax.count

class RoomBookingDBService(connectionProvider: ConnectionProvider) {

  implicit val session = AutoSession

  def book(bookID: String, userId: String, roomID: String, bookingTime: String): Int = {
    val booking = UserBooking.column
    withSQL {
      insert.into(UserBooking).columns(booking.bid, booking.uid, booking.rid, booking.bookingTime)
        .values(bookID, userId, roomID, bookingTime)
    }.update().apply()
  }

  def getRoom(roomID: String): Option[UserBooking] = {
    val userBookingTable = UserBooking.syntax("b")
    withSQL {
      select.from(UserBooking as userBookingTable).where.eq(userBookingTable.rid, roomID)
    }.map(UserBooking(userBookingTable.resultName)).single.apply()
  }

  def getCount(roomId: String): Long = {
    val bookTable = UserBooking.syntax("m")
    withSQL(
      select(count(bookTable.rid)).from(UserBooking as bookTable).where.eq(bookTable.rid, roomId)
    ).map(rs => rs.long(1)).single.apply().get
  }


}
