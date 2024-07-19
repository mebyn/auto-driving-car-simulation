package com.zuhlke.simulator

import com.zuhlke.simulator.vehicle.Car

class ControlCentre(
  private val field: Field,
  private val inputOperations: List<CarOperation>,
) {
  private val gridState: MutableMap<String, OperationState> =
    inputOperations
      .associate {
        it.car.name to OperationState(it.car)
      }.toMutableMap()
  private val copyOperations =
    inputOperations.map {
      it.copy(commands = ArrayDeque(it.commands))
    }

  fun runSimulation(): List<OperationState> {
    val operationQueue = ArrayDeque(copyOperations)
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
            val filterCollidedCars: (String) -> Map<String, OperationState> = {
              gridState
                .filter { (name, carState) -> name != it && carState.car.coordinate == car.coordinate }
            }
            val collidedCars = filterCollidedCars(car.name)
            gridState[car.name] =
              currentState.copy(
                car = car,
                collisionInfo = CollisionInfo(collidedCars = collidedCars.values.map { it.car }, step = step),
              )
            val removedCars =
              run {
                processingQueue.removeCollidedCars(collidedCars)
                operationQueue.removeCollidedCars(collidedCars)
              }
            removedCars.forEach { (name, state) ->
              gridState[name] =
                state.copy(
                  collisionInfo =
                    CollisionInfo(
                      collidedCars = filterCollidedCars(name).map { entry -> entry.value.car },
                      step = step,
                    ),
                )
            }
          }

          else -> gridState[car.name] = currentState.copy(car = car.move(commands.removeFirst()))
        }
        if (commands.isEmpty()) operationQueue.removeAt(indexAtQueue)
      }
      step++
    }
    return gridState.values.toList()
  }

  private fun ArrayDeque<CarOperation>.removeCollidedCars(collidedCars: Map<String, OperationState>) =
    collidedCars
      .map { (name, state) ->
        indexOfFirst { it.car.name == name }.takeIf { it >= 0 }?.let {
          removeAt(it)
        }
        name to state
      }.toMap()

  private val Car.hasCollision
    get() = gridState.any { it.key != name && it.value.car.coordinate == coordinate }
}

data class CarOperation(
  val car: Car,
  val commands: ArrayDeque<Command>,
)
