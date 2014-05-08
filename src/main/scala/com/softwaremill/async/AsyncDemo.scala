package com.softwaremill.async

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random

object AsyncDemo extends App {
  val start = System.currentTimeMillis()
  def info(msg: String) = printf("%.2f: %s\n", (System.currentTimeMillis() - start)/1000.0, msg)

}
