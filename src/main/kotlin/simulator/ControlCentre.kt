package com.zuhlke.simulator

import com.zuhlke.Car

class ControlCentre(
  private val field: Array<Array<Car?>>,
) {
  fun placeCar(car: Car): Array<Array<Car?>> {
    val (x, y) = car.coordinate
    runCatching {
      field[x][y] = car
    }.getOrElse {
      throw InvalidFieldState("Invalid coordinate provided. ${car.coordinate}")
    }
    return field
  }
}

enum class Operation {
  L, // Rotate car by 90 degrees left
  R, // Rotate car by 90 degrees right
  F, // Move car forward by 1 grid point
}

class InvalidFieldState(
  message: String,
) : Exception(message)
