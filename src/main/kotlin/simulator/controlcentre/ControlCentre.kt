package com.zuhlke.simulator.controlcentre

import com.zuhlke.simulator.Field
import com.zuhlke.simulator.vehicle.Car

class ControlCentre(
  private val field: Field,
  simulationInputs: List<SimulationInput>,
) {
  private val gridState: MutableMap<String, Car> =
    simulationInputs
      .associate {
        it.car.name to it.car
      }.toMutableMap()
  private val initialStates =
    simulationInputs.map {
      it.copy(commands = ArrayDeque(it.commands))
    }

  fun runSimulation(): List<Car> {
    val operationQueue = ArrayDeque(initialStates)
    var step = 0
    while (operationQueue.isNotEmpty()) {
      val processingQueue = ArrayDeque(operationQueue)
      while (processingQueue.isNotEmpty()) {
        val (car, commands) =
          processingQueue.removeFirst().let {
            val carInField = gridState[it.car.name] ?: throw IllegalStateException("Car should exist in the grid")
            carInField to it.commands
          }
        val indexAtQueue = operationQueue.indexOfFirst { it.car.name == car.name }
        when {
          car.hasCollision -> handleCollision(car, step)
          else -> gridState[car.name] = moveCar(car, commands.removeFirst())
        }
        if (commands.isEmpty() || gridState[car.name]?.collisionInfo != null) operationQueue.removeAt(indexAtQueue)
      }
      step++
    }
    return gridState.values.toList()
  }

  private fun handleCollision(
    car: Car,
    step: Int,
  ) {
    val filterCollidedCars: (String) -> Map<String, Car> = {
      gridState
        .filter { (name, carInField) -> name != it && carInField.coordinate == car.coordinate }
    }
    val collidedCars = filterCollidedCars(car.name)
    gridState[car.name] =
      car.copy(
        collisionInfo = CollisionInfo(collidedCars = collidedCars.values.map { it }, step = step),
      )
    collidedCars.forEach { (name, state) ->
      gridState[name] =
        state.copy(
          collisionInfo =
            CollisionInfo(
              collidedCars = filterCollidedCars(name).map { entry -> entry.value },
              step = step,
            ),
        )
    }
  }

  private fun moveCar(
    car: Car,
    command: Command,
  ): Car {
    val nextOrientation = car.orientation.move(command)
    return when {
      field.isCoordinateWithinBoundary(nextOrientation.coordinate) -> car.copy(orientation = nextOrientation)
      else -> car.copy(orientation = car.orientation.copy(direction = nextOrientation.direction))
    }
  }

  private val Car.hasCollision
    get() = gridState.any { (otherName, otherCar) -> otherName != name && otherCar.coordinate == coordinate }
}
