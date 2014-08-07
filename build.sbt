name := "keep-it-simple-scala"

version := "1.0"

scalaVersion := "2.11.2"

resolvers += "spray repo" at "http://repo.spray.io"

val sprayVersion = "1.3.1"

libraryDependencies ++= Seq(
  // akka
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  // spray
  "io.spray" %% "spray-routing" % sprayVersion,
  "io.spray" %% "spray-client" % sprayVersion,
  "io.spray" %% "spray-testkit" % sprayVersion % "test",
  // macwire
  "com.softwaremill.macwire" %% "macros" % "0.7",
  "com.softwaremill.macwire" %% "runtime" % "0.7",
  // async
  "org.scala-lang.modules" %% "scala-async" % "0.9.1",
  // util
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)