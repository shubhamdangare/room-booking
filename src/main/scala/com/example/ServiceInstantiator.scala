package com.example

import akka.actor.{ActorSystem, Scheduler}
import akka.event.LoggingAdapter
import akka.stream.ActorMaterializer
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext

class ServiceInstantiator(
                           val conf: Config,
                           private val defaultValuesConfig: Config,
                           dbLayers: DBLayer
                         )(
                           implicit system: ActorSystem,
                           val logger: LoggingAdapter,
                           materializer: ActorMaterializer
                         ) {

  implicit val ec: ExecutionContext = system.dispatcher
  implicit val scheduler: Scheduler = system.scheduler


  lazy val umService = new UserService(dbLayers.userDatabase)

  lazy val bookingService = new RoomBookingService(dbLayers.roomBooking,dbLayers.userDatabase)





}
