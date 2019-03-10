package com.example

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, UUID}

import scalikejdbc._

class UserDataDBService {

  val dBConnection = new DBConnection
  implicit val session = AutoSession
  dBConnection.createConnectiontoDB()

  def registerUsers(name: String, password: String, email: String): String = {
    val token = UUID.randomUUID().toString
    import java.util.Calendar
    val calendar = Calendar.getInstance
    val token_gen = new Timestamp(calendar.getTime.getTime)
    calendar.add(Calendar.MINUTE, 30)
    val token_exp = new Timestamp(calendar.getTime.getTime)
    val user = UserData.column
    withSQL {
      insert.into(UserData).columns(user.name, user.password, user.email, user.token, user.token_gen, user.token_exp)
        .values(name, password, email, token, token_gen, token_exp)
    }.update().apply()
    token
  }

  def signInUser(name: String, password: String): Option[String] = {
    val token = UUID.randomUUID().toString
    import java.util.Calendar
    val calendar = Calendar.getInstance
    val token_gen = new Timestamp(calendar.getTime.getTime)
    calendar.add(Calendar.MINUTE, 30)
    val token_exp = new Timestamp(calendar.getTime.getTime)
    val userTable = UserData.syntax("m")
    val valid: Option[UserData] = withSQL {
      select.from(UserData as userTable).where.eq(userTable.name, name).and.eq(userTable.password, password)
    }.map(UserData(userTable.resultName)).single.apply()

    if (valid.isDefined) {
      withSQL {
        update(UserData).set(
          UserData.column.token -> token,
          UserData.column.token_gen -> token_gen,
          UserData.column.token_exp -> token_exp
        ).where.eq(UserData.column.name, name)
      }.update.apply()
      Option(token.toString)
    }
    else {
      Option("Invalid UserName or Password or not registered")
    }
  }

  def getCalendarTimeStamp(token_exp: Any): Timestamp = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
    val parsedDate = dateFormat.parse(token_exp.toString)
    new Timestamp(parsedDate.getTime)
  }

  def getUserData(userId: Int): Option[UserData] = {
    val userTable = UserData.syntax("u")
    withSQL {
      select.from(UserData as userTable).where.eq(userTable.uid, userId)
    }.map(UserData(userTable.resultName)).single().apply()
  }

  def checkBookingDoneAlready(roomID: Int): Option[UserBooking] = {
    val userBookingTable = UserBooking.syntax("b")
    withSQL {
      select.from(UserBooking as userBookingTable).where.eq(userBookingTable.rid, roomID)
    }.map(UserBooking(userBookingTable.resultName)).single.apply()
  }

  def checkIfRoomBooked(userId: Int, roomID: Int, bookingTime: String): Int = {
    val booking = UserBooking.column
    withSQL {
      insert.into(UserBooking).columns(booking.uid, booking.rid, booking.bookingTime)
        .values(userId, roomID, bookingTime)
    }.update().apply()
  }

  def getNumberOfUser(): Long  = {
    val userTable = UserData.syntax("m")
    withSQL(select(sqls.count).from(UserData as userTable)).map(rs => rs.long(1)).single.apply().get
  }

  def find(id: Int): Option[UserData] = {
    val userTable = UserData.syntax("m")
    withSQL {
      select.from(UserData as userTable).where.eq(userTable.uid, id)
    }.map(UserData(userTable.resultName)).single.apply()
  }

  def findAll(): List[UserBooking] = {
    val userBookingTable = UserBooking.syntax("b")
    withSQL(select.from(UserBooking as userBookingTable)).map(UserBooking(userBookingTable.resultName)).list.apply()
  }

  def destroy(roomID: Int): Int = {
    withSQL { delete.from(UserBooking).where.eq(UserBooking.column.rid, roomID) }.update.apply()
  }


  def roomBooking(bookingTime: String, userId: Int, roomID: Int): String = {
    val userDetail = getUserData(userId)
    if (userDetail.isDefined) {
      val token_exp = userDetail match {
        case Some(value) => value.token_exp
        case None => None
      }
      if (new Timestamp(Calendar.getInstance.getTime.getTime).before(getCalendarTimeStamp(token_exp))) {
        val bookingCheck = checkBookingDoneAlready(roomID)
        if (bookingCheck.isEmpty) {
          if (bookingTime.toInt < 5) {
            checkIfRoomBooked(userId, roomID, bookingTime).toString
          }
          else {
            "maximum time allocated cannot book for that long"
          }
        }
        else {
          "you can book after" + bookingCheck.get.bookingTime.toString + "hours"
        }
      }
      else {
        "Token has Expired"
      }
    }
    else {
      "user not found"
    }
  }
}

