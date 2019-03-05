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
    val token_exp = calendar.add(Calendar.MINUTE, 30)
    withSQL {
      insert.into(UserData).values(name, password, email, token, token_gen, token_exp)
    }.update().apply()
    token
  }

}

