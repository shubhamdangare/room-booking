package com.example

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, UUID}

import scalikejdbc._
import scalikejdbc.interpolation.SQLSyntax.count

class UserDataDBLayer(connectionProvider: ConnectionProvider) {


  implicit val session = AutoSession

  def create(name: String, password: String, email: String, uid: String): Int = {
    val token = UUID.randomUUID().toString
    val calendar = Calendar.getInstance
    calendar.add(Calendar.MINUTE, 30)
    val token_exp = new Timestamp(calendar.getTime.getTime)
    val user = UserData.column
    withSQL {
      insert.into(UserData).columns(user.name, user.password, user.email, user.token, user.token_exp, user.uid)
        .values(name, password, email, token, token_exp, uid)
    }.update().apply()
  }

  def getCount(email: String): Long = {
    val userTable = UserData.syntax("m")
    withSQL(
      select(count(userTable.email)).from(UserData as userTable).where.eq(userTable.email, email)
    ).map(rs => rs.long(1)).single.apply().get
  }

  def get(email: String, password: String): Option[UserData] = {
    val userTable = UserData.syntax("m")
    withSQL {
      select.from(UserData as userTable).where.eq(userTable.email, email).and.eq(userTable.password, password)
    }.map(UserData(userTable.resultName)).single.apply()
  }

  def get(uid: String): Option[UserData] = {
    val userTable = UserData.syntax("m")
    withSQL {
      select.from(UserData as userTable).where.eq(userTable.uid, uid)
    }.map(UserData(userTable.resultName)).single.apply()
  }

}

