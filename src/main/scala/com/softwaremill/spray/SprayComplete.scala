package com.softwaremill.spray

import spray.routing.{RequestContext, Route, SimpleRoutingApp}
import akka.actor.{Props, Actor, ActorSystem}

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
      pathPrefix("takeaway") {
        path("hello") {
          complete {
            "Welcome to the potato & steak take-away!"
          }
        } ~
        path("order" / "potatoes") {
          parameters("mashed".as[Boolean], "number".as[Int], "special"?) { (mashed, number, specialWishes) =>
            complete {
              s"You ordered ${if (mashed) "mashed" else "normal"} potatoes. " +
                s"One is free, so you'll get ${number+1} potatoes." +
                s"Your special wishes: ${specialWishes.getOrElse("none luckily")}"
            }
          }
        } ~
        path("pay") {
          secure {
            ctx => paymentActor ! ctx
          }
        }
      }
    }
  }

  class PaymentActor extends Actor {
    def receive = {
      case ctx: RequestContext => ctx.complete("paid")
    }
  }

  lazy val paymentActor = actorSystem.actorOf(Props[PaymentActor])
}
