package com.softwaremill.async

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.async.Async._
import scala.util.Random

object AsyncComplete extends App {
  val start = System.currentTimeMillis()
  def info(msg: String) = printf("%.2f: %s\n", (System.currentTimeMillis() - start)/1000.0, msg)
  def section(msg: String) = printf("\n%s\n------\n", msg)

  case class Dish(name: String) {
    def +(other: Dish) = Dish(s"$name with ${other.name}")
  }

  def cook(what: String) = {
    // ideally we would have async I/O with the stove/oven
    Thread.sleep(1000L)
    info(s"$what cooked!")
    Dish(what)
  }

  def serve(dish: Dish) = {
    info(s"Here's your ${dish.name}, sir.")
  }

  section("Sync")
  val steak = cook("steak")
  val potatoes = cook("potatoes")

  // steak will be cold
  serve(steak+potatoes)

  section("Futures 1")
  val f1 = for {
    s <- Future { cook("steak") }
    p <- Future { cook("potatoes") }
  } yield {
    serve(s + p)
  }
  Await.result(f1, 10.seconds)

  section("Futures 1 bis")
  val f1bis = Future { cook("steak") }.flatMap { s =>
    Future { cook("potatoes") }.map { p =>
      serve(s + p)
    }
  }
  Await.result(f1bis, 10.seconds)

  section("Futures 2")
  val fs = Future { cook("steak") }
  val fp = Future { cook("potatoes") }

  val f2 = for {
    s <- fs
    p <- fp
  } yield {
    serve(s + p)
  }
  Await.result(f2, 10.seconds)

  section("Async 1 - optional sidedish")
  def wantsSidedish() = Random.nextBoolean()

  val f3 = async {
    val fs = async { cook("steak") }

    val dish = if (wantsSidedish()) {
      val fp = async { cook("potatoes") }
      await(fs) + await(fp)
    } else {
      await(fs)
    }

    serve(dish)
  }
  Await.result(f3, 10.seconds)

  section("Async 2 - async sidedish")
  def wantsRice() = Future { Thread.sleep(1000L); Random.nextBoolean() }

  val f4 = async {
    val fs = async { cook("steak") }
    val fp = async { cook("potatoes") } // always preparing potatoes

    val dish = if (await(wantsRice())) {
      val rice = cook("rice")
      await(fs) + rice
    } else {
      await(fs) + await(fp)
    }

    serve(dish)
  }
  Await.result(f4, 10.seconds)
}
