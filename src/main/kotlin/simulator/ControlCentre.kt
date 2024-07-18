package com.zuhlke.simulator

class ControlCentre(
  val field: Field,
  val initialGarageState: List<CarEntry>,
) {
  fun runSimulation(): List<CarEntry> {
    val carsOnField = ArrayDeque(initialGarageState)
    while (carsOnField.isNotEmpty()) {
      for (i in 0..<carsOnField.size) {
        val (car, commands) = carsOnField[i]
        car.move(commands.removeFirst())
        carsOnField.removeIf { commands.isEmpty() }
      }
    }
    initialGarageState.forEach { (car, command) ->
      println(
        "Simulating - ${car.name}, (${car.coordinate.x}, ${car.coordinate.y}), ${car.direction.name}, ${
          command.joinToString(
            "",
          )
        } ",
      )
    }
    return carsOnField
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
