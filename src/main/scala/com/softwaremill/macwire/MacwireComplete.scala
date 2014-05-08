package com.softwaremill.macwire

object MacwireComplete extends App with Macwire {
  case class Field()
  case class Digger()
  case class PotatoFarm(field: Field, digger: Digger) {
    println("New potato farm! Rejoice!") // added later
  }

  case class CowPasture(potatoFarm: PotatoFarm) // dependency added later
  case class Meatery(cowPasture: CowPasture)

  case class Restaurant(potatoFarm: PotatoFarm, meatery: Meatery) {
    def orderSteakWithPotatoes() = {
      println(s"Welcome to $this. Here's your order.")
    }
  }

  // initially no modules
  trait CropModule {
    def potatoFarm: PotatoFarm
  }

  trait TraditionalCropModule extends CropModule {
    lazy val field = wire[Field]

    // initially val
    def potatoFarm = wire[PotatoFarm]

    // changed later to def
    lazy val digger = wire[Digger]
  }

  trait LivestockModule {
    lazy val cowPasture = wire[CowPasture]
    lazy val meatery = wire[Meatery]

    def potatoFarm: PotatoFarm
  }

  trait RestaurantModule extends CropModule with LivestockModule {
    lazy val restaurant = wire[Restaurant]
  }

  val app = new TraditionalCropModule with LivestockModule with RestaurantModule
  app.restaurant.orderSteakWithPotatoes()
}
