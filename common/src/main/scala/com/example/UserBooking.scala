package com.example


import scalikejdbc._

case class UserBooking(bid: String, uid: String, rid: String, bookingTime: String)


object UserBooking extends SQLSyntaxSupport[UserBooking] {

  override val tableName = "Booking"
  override val useSnakeCaseColumnName = false

  override val nameConverters = Map(
    "^bid$" -> "bid",
    "^uid$" -> "uid",
    "^rid$" -> "rid",
    "^bookingTime$" -> "bookingTime",
  )

  def apply(e: SyntaxProvider[UserBooking])(rs: WrappedResultSet): UserBooking = apply(e.resultName)(rs)

  def apply(e: ResultName[UserBooking])(rs: WrappedResultSet): UserBooking =
    new UserBooking(bid = rs.string(e.bid), uid = rs.string(e.uid), rid = rs.string(e.rid),
      bookingTime = rs.string(e.bookingTime))
}