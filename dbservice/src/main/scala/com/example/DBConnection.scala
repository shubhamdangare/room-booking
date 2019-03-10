package com.example

import scalikejdbc._

class DBConnection {

  def createConnectiontoDB(): Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mysql://localhost:3306/Dwag", "root", "l3n60888")
  }
}
