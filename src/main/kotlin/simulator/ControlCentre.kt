package com.zuhlke.simulator

class ControlCentre(
  private val field: Field,
  private val inputOperations: List<CarOperation>,
) {
  private val gridState: MutableMap<String, CarState> =
    inputOperations
      .associate {
        it.car.name to CarState(it.car, emptyList())
      }.toMutableMap()

  fun runSimulation(): List<CarState> {
    val operationQueue = ArrayDeque(inputOperations)
    var step = 0
    while (operationQueue.isNotEmpty()) {
      val processingQueue = ArrayDeque(operationQueue)
      while (operationQueue.isNotEmpty() && processingQueue.isNotEmpty()) {
        val (carToProcess, commands) = processingQueue.removeFirst()
        val currentState = gridState[carToProcess.name] ?: throw IllegalStateException("Car should exist in the grid")
        val car = (currentState.car)
        val indexAtQueue = operationQueue.indexOfFirst { it.car.name == car.name }
        when {
          !field.isCoordinateWithinBoundary(car.coordinate) -> operationQueue.removeAt(indexAtQueue)

          car.hasCollision -> {
            operationQueue.removeAt(indexAtQueue)
            val filterCollidedCars: (String) -> Map<String, CarState> = {
              gridState
                .filter { (name, carState) -> name != it && carState.car.coordinate == car.coordinate }
            }
            val collidedCars = filterCollidedCars(car.name)
            gridState[car.name] =
              currentState.copy(car = car, collidedCars = collidedCars.values.map { it.car })
            operationQueue.removeCollidedCars(collidedCars, filterCollidedCars)
          }

          else -> gridState[car.name] = currentState.copy(car = car.move(commands.removeFirst()))
        }
        if (commands.isEmpty()) operationQueue.removeAt(indexAtQueue)
      }
      step++
    }
    return gridState.values.toList()
  }

  private fun ArrayDeque<CarOperation>.removeCollidedCars(
    collidedCars: Map<String, CarState>,
    filterCollidedCars: (String) -> Map<String, CarState>,
  ) {
    collidedCars.forEach { (name, state) ->
      removeAt(indexOfFirst { it.car.name == name })
      gridState[name] = state.copy(collidedCars = filterCollidedCars(name).map { it.value.car })
    }
  }

  private val Car.hasCollision
    get() = gridState.any { it.key != name && it.value.car.coordinate == coordinate }
}

data class CarState(
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
