package com.example

import java.util.UUID

import org.mockito._
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

class UserDataDBServiceSpec extends WordSpec with ScalaFutures with Matchers with MockitoSugar with BeforeAndAfter {


  val userDBService = new UserDataDBService
  val dbMock = mock[DBConnection]

  "Registration of new User" should {
    "successFully registered" in {
      val token = UUID.randomUUID().toString

      when(userDBService.registerUsers("abc", "abc,", "abc@email")).thenReturn(token)

    }
  }
}
