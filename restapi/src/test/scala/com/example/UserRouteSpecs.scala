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


}
