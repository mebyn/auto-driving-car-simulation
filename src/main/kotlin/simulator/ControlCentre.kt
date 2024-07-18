package com.zuhlke.simulator

class ControlCentre(
  private val field: Field,
  private val initialGarageState: List<CarEntry>,
) {
  private val gridState: MutableMap<String, Car> =
    initialGarageState
      .associate {
        it.car.name to it.car
      }.toMutableMap()

  // TODO: Collision detection
  // TODO: Collision detection at car insertion step
  fun runSimulation(): List<Car> {
    val carsOnField = initialGarageState.toMutableList()
    while (carsOnField.isNotEmpty()) {
      for (i in 0..<carsOnField.size) {
        val (initialCarState, commands) = carsOnField[i]
        val carNameInScope = initialCarState.name
        val currentCarState = gridState[carNameInScope] ?: initialCarState
        val newCarState = currentCarState.move(commands.removeFirst())
        when {
          !field.isCoordinateWithinBounds(newCarState.coordinate)
          -> carsOnField.removeIf { it.car.name == carNameInScope }

          else -> gridState[carNameInScope] = newCarState
        }
        carsOnField.removeIf { commands.isEmpty() }
      }
    }
    return gridState.values.toList()
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
