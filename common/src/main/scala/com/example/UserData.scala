package com.example

import java.sql.Timestamp

import scalikejdbc._

case class UserData(
                     uid: String,
                     name: String,
                     password: String,
                     email: String,
                     token: String,
                     token_exp: Timestamp,

                   )

object UserData extends SQLSyntaxSupport[UserData] {

  override val tableName = "UserData"
  override val useSnakeCaseColumnName = false

  override val nameConverters: Map[String, String] = Map(
    "^uid$" -> "uid",
    "^name$" -> "name",
    "^password$" -> "password",
    "^email$" -> "email",
    "^token_exp$" -> "token_exp"
  )

  def apply(e: SyntaxProvider[UserData])(rs: WrappedResultSet): UserData = apply(e.resultName)(rs)

  def apply(e: ResultName[UserData])(rs: WrappedResultSet): UserData =
    new UserData(name = rs.string(e.name), password = rs.string(e.password),
      email = rs.string(e.email), token = rs.string(e.token),
      token_exp = rs.timestamp(e.token_exp), uid = rs.string(e.uid))
}
