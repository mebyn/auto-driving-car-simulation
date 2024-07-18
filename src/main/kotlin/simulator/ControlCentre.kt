package com.zuhlke.simulator

class ControlCentre(
  private val field: Field,
  private val initialGarageState: List<CarOperation>,
) {
  private val gridState: MutableMap<String, Car> =
    initialGarageState
      .associate {
        it.car.name to it.car
      }.toMutableMap()

  fun runSimulation(): List<Car> {
    val carsOnField = ArrayDeque(initialGarageState)
    var step = 0
    while (carsOnField.isNotEmpty()) {
      val processingQueue = ArrayDeque(carsOnField)
      while (processingQueue.isNotEmpty() && carsOnField.isNotEmpty()) {
        val (initialCarState, commands) = processingQueue.removeFirst()
        val currentCarState = gridState[initialCarState.name] ?: initialCarState
        currentCarState.drive(carsOnField)
        if (carsOnField.isNotEmpty()) {
          val nextCarState = currentCarState.move(commands.removeFirst())
          nextCarState.drive(carsOnField)
        }
        if (commands.isEmpty()) {
          carsOnField.removeFromProcessing(initialCarState.name)
          // Add to Result
        }
      }
      step++
    }
    return gridState.values.toList()
  }

  private fun Car.drive(carsOnField: ArrayDeque<CarOperation>) {
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

  private fun ArrayDeque<CarOperation>.handleCollision(
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

  private fun ArrayDeque<CarOperation>.removeFromProcessing(carNameInScope: String) =
    removeAt(indexOfFirst { it.car.name == carNameInScope })
}

data class SimulationResult(
  val car: Car,
  val collidedCars: List<Car>,
)

data class CarOperation(
  val car: Car,
  val commands: ArrayDeque<Command>,
)

enum class Command {
  L, // Rotate car by 90 degrees left
  R, // Rotate car by 90 degrees right
  F, // Move car forward by 1 grid point
}
