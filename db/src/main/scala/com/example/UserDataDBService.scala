package com.example

import java.sql.{Timestamp}
import java.util.UUID

import scalikejdbc._

class UserDataDBService {

  val dBConnection = new DBConnection

  def registerUsers(name: String, password: String, email: String): String = {
    implicit val session = AutoSession
    dBConnection.createConnectiontoDB()
    val token = UUID.randomUUID().toString
    import java.util.Calendar
    val calendar = Calendar.getInstance
    val token_gen = new Timestamp(calendar.getTime.getTime)
    calendar.add(Calendar.MINUTE, 30)
    val token_exp = new Timestamp(calendar.getTime.getTime)
    val user = UserData.column
    withSQL {
      insert.into(UserData).columns(user.name,user.password,user.email,user.token,user.token_gen,user.token_exp)
        .values(name, password, email, token, token_gen, token_exp)
    }.update().apply()
    token
  }

  def signInUser(name: String, password: String): Option[String] = {
    implicit val session = AutoSession
    dBConnection.createConnectiontoDB()

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
    else{
      Option("Invalid UserName or Password")
    }
  }

}
