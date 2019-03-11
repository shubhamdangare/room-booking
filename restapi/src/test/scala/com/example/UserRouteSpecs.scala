package com.example

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.scalatest.WordSpec
import org.scalatest.mockito.MockitoSugar
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.Matchers
import org.mockito.Mockito.when
import play.api.libs.json.Json
import scalikejdbc.ConnectionPool

class UserRouteSpecs extends WordSpec with PlayJsonSupport with MockitoSugar with ScalatestRouteTest with Matchers {
/**
  val dbMock = mock[DBConnection]
  val userDBService = mock[UserDataDBService]
  val userRoute  = UserRoutes.route
  val connectionPool = Mockito.mock(classOf[ConnectionPool], RETURNS_MOCKS)

  "POST /register-user" should {
    "return SUCCESS response from signin when user signIn by username " in {
      val data = UUID.randomUUID().toString
      when(dbMock.createConnectiontoDB).thenReturn(None)
      when(userDBService.registerUsers("username", "password", "email@gmial.com"))
        .thenReturn(data)
      Post("/register-user",Json.parse(s"""{"response":"${data}"}""".stripMargin)) ~> Route.seal(userRoute) ~> check {
        status shouldEqual StatusCodes.OK
      }

    }

  }
  */
}
