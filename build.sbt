name := "RoomAllocation"
version := "0.1"
organization in ThisBuild := "com.example"
scalaVersion in ThisBuild := "2.12.8"


lazy val root = (project in file("."))
  .dependsOn()
  .aggregate()
  .settings(
    name := "root",
    libraryDependencies ++= commonDependencies
  )

lazy val common = (project in file("common"))
  .settings(
      name := "common",
      libraryDependencies ++= commonDependencies ++ Seq("com.typesafe.play" %% "play-json" % "2.7.1")
  )

lazy val db = (project in file("db"))
  .aggregate(common)
  .dependsOn(common)
  .settings(
      name := "DB",
      libraryDependencies ++= commonDependencies ++ Seq("com.google.inject" % "guice" % "4.2.2")
  )


lazy val restapi = (project in file("restapi"))
  .aggregate(common, db)
  .dependsOn(common, db)
  .settings(
    name := "restapi",
    libraryDependencies ++= commonDependencies ++ Seq("com.typesafe.akka" %% "akka-http" % "10.1.7",
      "com.typesafe.akka" %% "akka-stream" % "2.5.19","com.typesafe" % "config" % "1.2.1"
    )
  )


lazy val dependencies = new {

    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    val scalaMockito = "org.mockito" % "mockito-all" % "1.8.4"
    val mysql = "mysql" % "mysql-connector-java" % "5.1.12"
    val scalike = "org.scalikejdbc" %% "scalikejdbc" % "3.3.2"
    val akkaJson = "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7"
    //val playJson   = "com.typesafe.play" %% "play-json" % "2.7.1"
    val akkaHttpPlayJson = "de.heikoseeberger" %% "akka-http-play-json" % "1.20.0"
   // val scalaLogger = "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
    val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
}


lazy val commonDependencies = Seq(
    dependencies.scalaTest,
    dependencies.scalaMockito,
    dependencies.mysql,
    dependencies.scalike,
    //  dependencies.playJson,
    dependencies.akkaJson,
    dependencies.akkaHttpPlayJson,
   // dependencies.scalaLogger,
    dependencies.logback
)
