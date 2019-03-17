package com.example

import java.sql.Timestamp
import java.util.Calendar

import com.example.UserService.SignInError.InvalidCredentials
import com.example.UserService.SignUpError.UserNotCreated
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class UserServiceSpec extends WordSpec with ScalaFutures with Matchers with MockitoSugar with BeforeAndAfter {

  val mockedUsedDB = mock[UserDataDBLayer]
  val userService = new UserService(mockedUsedDB)
  val calendar = Calendar.getInstance
  val token_exp = new Timestamp(calendar.getTime.getTime)


  "UserService" should {
    "User not created" in {
      when(mockedUsedDB.getCount("asdasda")).thenReturn(1.toLong)
      when(mockedUsedDB.create("asdasda", "Asdas", "asdasd", "asdas")).
        thenReturn(1)
      whenReady(userService.registerUsers("asda", "Asd0", "Asdasd")) {
        response => response shouldBe Left(UserNotCreated)
      }
    }
    "sign User" in {
      when(mockedUsedDB.get("asdasda", "asdas")).thenReturn(Option(new UserData(
        "sadas",
        "sdasdas",
        "asdasda",
        "ASDasdas",
        "asdasdasd",
        token_exp)))
      whenReady(userService.signInUser("ASDasdas", "asdasda")) {
        response => response shouldBe Left(InvalidCredentials)
      }
    }
  }
}
