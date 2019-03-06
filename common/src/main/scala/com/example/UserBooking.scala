package com.example


import java.sql.Timestamp

import scalikejdbc._

case class UserBooking(bid: Int, timeto: Timestamp, timefrom: Timestamp, uid: Int, rid: Int)


object UserBooking extends SQLSyntaxSupport[UserBooking] {

  override val tableName = "Booking"
  override val useSnakeCaseColumnName = false

  override val nameConverters = Map(
    "^bid$" -> "bid",
    "^timeto$" -> "timeto",
    "^timefrom$" -> "timefrom",
    "^uid$" -> "uid",
    "^rid$" -> "rid",
  )

  def apply(e: SyntaxProvider[UserBooking])(rs: WrappedResultSet): UserBooking = apply(e.resultName)(rs)

  def apply(e: ResultName[UserBooking])(rs: WrappedResultSet): UserBooking =
    new UserBooking(bid = rs.int(e.bid), timeto = rs.timestamp(e.timeto),
      timefrom = rs.timestamp(e.timefrom), uid = rs.int(e.uid), rid = rs.int(e.rid))
}