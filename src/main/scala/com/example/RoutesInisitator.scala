package com.example

import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives.{concat, ignoreTrailingSlash}
import akka.http.scaladsl.server.Route
import com.typesafe.config.Config
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings

import scala.concurrent.ExecutionContext

class RoutesInisitator(
                        conf: Config,
                        services: ServiceInstantiator,
                        appUrl: String
                      )(
                        implicit val ec: ExecutionContext
                      ) extends PlayJsonSupport {

  private val umRoutes = new UserRoute(services.umService)
  private val bookingRoute = new BookingRoute(services.bookingService)
  private val corsSettings = CorsSettings.defaultSettings
    .withAllowedMethods(scala.collection.immutable.Seq(GET, POST, PUT, HEAD, DELETE, OPTIONS))

  val routes: Route = cors(corsSettings) {
    ignoreTrailingSlash {
      concat(
        umRoutes.route,
        bookingRoute.route
      )
    }
  }
}
