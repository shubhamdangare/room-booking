package com.example

import scalikejdbc._

case class RoomData(rid: Int, roomType: String)

object RoomData extends SQLSyntaxSupport[RoomData] {
  override val tableName = "UserData"
  override val useSnakeCaseColumnName = false

  override val nameConverters = Map(
    "^rid$" -> "rid",
    "^type$" -> "type"
  )

  def apply(e: SyntaxProvider[RoomData])(rs: WrappedResultSet): RoomData = apply(e.resultName)(rs)

  def apply(e: ResultName[RoomData])(rs: WrappedResultSet): RoomData =
    new RoomData(rid = rs.int(e.rid),roomType =rs.string(e.roomType) )
}


