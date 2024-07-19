package com.zuhlke.simulator.controlcentre

import com.zuhlke.simulator.vehicle.Car

data class OperationState(
  val car: Car,
  val collisionInfo: CollisionInfo? = null,
)

data class CollisionInfo(
  val collidedCars: List<Car> = emptyList(),
  val step: Int = 0,
)
