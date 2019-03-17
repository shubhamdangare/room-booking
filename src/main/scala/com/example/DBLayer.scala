package com.example

class DBLayer(connectionProvider: ConnectionProvider) {


  lazy val userDatabase: UserDataDBLayer = new UserDataDBLayer(connectionProvider)

  lazy val roomBooking:RoomBookingDBService = new RoomBookingDBService(connectionProvider)

}
