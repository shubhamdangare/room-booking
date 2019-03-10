package com.example

import java.sql.Timestamp
import java.util.Calendar

import org.specs2.mutable._
import scalikejdbc.specs2.mutable.AutoRollback

class UserDataDBServiceSpec extends Specification {

  val userDataDBService = new UserDataDBService;
  "count all records" in new AutoRollback {
    val count = userDataDBService.getNumberOfUser()
    count should be_>(0L)
  }

  "get user data" in new AutoRollback {
    val data = userDataDBService.getUserData(3)
    data.isDefined should beTrue
  }
  "check room booked already" in new AutoRollback {
    val data = userDataDBService.checkBookingDoneAlready(303)
    data.isDefined should beTrue
  }
  "check is booking is not empty" in new AutoRollback {
    val data = userDataDBService.findAll()
    data.isEmpty should beFalse
  }
  "check is booked room is deallocated" in new AutoRollback {
    val data = userDataDBService.destroy(301)
    assert(data == 0)
  }
  "covert time stamp" in new AutoRollback {
    val calendar = Calendar.getInstance
    val token_gen = new Timestamp(calendar.getTime.getTime)
    val data = userDataDBService.getCalendarTimeStamp(token_gen.toString)
    assert(data == token_gen)
  }

  "room booking for limited time" in new AutoRollback {
    val data = userDataDBService.roomBooking("6", 3, 303)
    assert(data != "maximum time allocated cannot book for that long")
  }
  "user not registered" in new AutoRollback {
    val data = userDataDBService.roomBooking("1", 10, 303)
    assert(data == "user not found")
  }

}
