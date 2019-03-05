package com.example

import java.sql.{Timestamp}
import java.util.UUID

import scalikejdbc._

class UserDataDBService {

  val dBConnection = new DBConnection

  def addUsers(name: String, password: String, email: String): String = {
    implicit val session = AutoSession
    dBConnection.createConnectiontoDB()
    val token = UUID.randomUUID().toString
    import java.util.Calendar
    val calendar = Calendar.getInstance
    val token_gen = new Timestamp(calendar.getTime.getTime)
    calendar.add(Calendar.MINUTE, 30)
    val token_exp = new Timestamp(calendar.getTime.getTime)
    withSQL {
      insert.into(UserData).values(name, password, email, token, token_gen, token_exp)
    }.update().apply()
    token
  }

}

