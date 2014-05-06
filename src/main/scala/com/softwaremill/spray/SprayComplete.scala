package com.softwaremill.spray

import spray.routing.{Route, SimpleRoutingApp}
import akka.actor.ActorSystem

object SprayComplete extends App with SimpleRoutingApp {
  implicit val actorSystem = ActorSystem()

  def secure(route: Route): Route = {
    parameters("username", "password") { (username, password) =>
      if (username == "admin" && password == "1234") {
        route
      } else {
        ctx => ctx.complete(401, "bad user")
      }
    }
  }

  startServer(interface = "localhost", port = 8080) {
    get {
      pathPrefix("geecon") {
        path("hello") {
          complete {
            "Hello World!"
          }
        } ~
        path("parameters" / "demo") {
          parameters("name"?, "age".as[Int]) { (name, age) =>
            complete {
              s"Hello, ${name.getOrElse("stranger")}. In a year you'll be ${age+1}."
            }
          }
        } ~
        path("secure") {
          secure {
            complete {
              "secret"
            }
          }
        }
      }
    }
  }
}
