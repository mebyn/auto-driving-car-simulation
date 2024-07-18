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
    val carsOnField = ArrayDeque(initialGarageState)
    while (carsOnField.isNotEmpty()) {
      val currentQueue = ArrayDeque(carsOnField)
      while (currentQueue.isNotEmpty()) {
        val (initialCarState, commands) = currentQueue.removeFirst()
        val carNameInScope = initialCarState.name
        val currentCarState = gridState[carNameInScope] ?: initialCarState
        val newCarState = currentCarState.move(commands.removeFirst())
        when {
          !field.isCoordinateWithinBounds(newCarState.coordinate)
          -> carsOnField.removeAt(carsOnField.indexOfFirst { it.car.name == carNameInScope })
          // Collision Detect
          gridState.values.any { it.name != carNameInScope && it.coordinate == newCarState.coordinate } -> {
            gridState.values
              .filter { it.name != carNameInScope && it.coordinate == newCarState.coordinate }
              .forEach { collidedCar ->
                carsOnField.removeAt(carsOnField.indexOfFirst { it.car.name == collidedCar.name })
              }
            carsOnField.removeAt(carsOnField.indexOfFirst { it.car.name == carNameInScope })
            gridState[carNameInScope] = newCarState
          }

          else -> gridState[carNameInScope] = newCarState
        }
        if (commands.isEmpty()) carsOnField.removeAt(carsOnField.indexOfFirst { it.car.name == carNameInScope })
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
