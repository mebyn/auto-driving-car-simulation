package com.zuhlke.simulator

class ControlCentre(
  val garage: List<CarEntry>,
) {
  fun runSimulation() {
    garage.forEach { (car, command) ->
      println(
        "Simulating - ${car.name}, (${car.coordinate.x}, ${car.coordinate.y}), ${car.direction.name}, ${
          command.joinToString(
            "",
          )
        } ",
      )
    }
  }
}

data class CarEntry(
  val car: Car,
  val commands: ArrayDeque<Operation>,
)

enum class Operation {
  L, // Rotate car by 90 degrees left
  R, // Rotate car by 90 degrees right
  F, // Move car forward by 1 grid point
}
