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

  // TODO: Collision detection at car insertion step
  fun runSimulation(): List<Car> {
    val carsOnField = ArrayDeque(initialGarageState)
    var step = 0
    while (carsOnField.isNotEmpty()) {
      val processingQueue = ArrayDeque(carsOnField)
      while (processingQueue.isNotEmpty()) {
        val (initialCarState, commands) = processingQueue.removeFirst()
        val currentCarState = gridState[initialCarState.name] ?: initialCarState
        currentCarState.drive(carsOnField)
        val nextCarState = currentCarState.move(commands.removeFirst())
        nextCarState.drive(carsOnField)
        if (commands.isEmpty()) {
          carsOnField.removeFromProcessing(initialCarState.name)
          // Add to Result
        }
      }
      step++
    }
    return gridState.values.toList()
  }

  private fun Car.drive(carsOnField: ArrayDeque<CarEntry>) {
    val detectCollision: (String, Coordinate) -> Boolean =
      { name, coordinate ->
        gridState.values.any { it.name != name && it.coordinate == coordinate }
      }
    when {
      !field.isCoordinateWithinBounds(coordinate)
      -> {
        carsOnField.removeFromProcessing(name)
        // Add to result
      }
      // Collision Detect
      detectCollision(name, coordinate) -> {
        carsOnField.handleCollision(name, this)
      }

      else -> gridState[name] = this
    }
  }

  private fun ArrayDeque<CarEntry>.handleCollision(
    carNameInScope: String,
    carState: Car,
  ) {
    gridState.values
      .filter { it.name != carNameInScope && it.coordinate == carState.coordinate }
      .forEach { collidedCar ->
        removeFromProcessing(collidedCar.name)
      }
    removeFromProcessing(carNameInScope)
    gridState[carNameInScope] = carState
  }

  private fun ArrayDeque<CarEntry>.removeFromProcessing(carNameInScope: String) = removeAt(indexOfFirst { it.car.name == carNameInScope })
}

data class SimulationResult(
  val car: Car,
  val collidedCars: List<Car>,
)

data class CarEntry(
  val car: Car,
  val commands: ArrayDeque<Operation>,
)

enum class Operation {
  L, // Rotate car by 90 degrees left
  R, // Rotate car by 90 degrees right
  F, // Move car forward by 1 grid point
}
