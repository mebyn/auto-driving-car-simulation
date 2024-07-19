package com.zuhlke.simulator.console

import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.Field
import com.zuhlke.simulator.controlcentre.Command
import com.zuhlke.simulator.controlcentre.ControlCentre
import com.zuhlke.simulator.controlcentre.Operation
import com.zuhlke.simulator.initializeField
import com.zuhlke.simulator.vehicle.Car
import com.zuhlke.simulator.vehicle.Direction

object SimulatorConsole {
  fun start() {
    while (true) {
      println("Welcome to Auto Driving Car Simulation!\n")
      val field =
        parseConsoleInput {
          print("Please enter the width and height of the simulation field in x y format: ")
          initializeField(readlnOrNull())
        }

      println("You have created a field of ${field.width} x ${field.height}\n")
      when (simulate(field)) {
        ConsoleCommand.START_OVER -> continue
        ConsoleCommand.EXIT -> return
      }
    }
  }

  private fun simulate(field: Field): ConsoleCommand {
    var operations = emptyList<Operation>()
    while (true) {
      println(
        """
        Please choose from the following options:
        [1] Add a car to field
        [2] Run simulation
        """.trimIndent(),
      )
      print("Your input: ")
      runCatching {
        when (readlnOrNull()) {
          "1" -> operations = addNewOperation(operations)
          "2" -> return runSimulation(field, operations).let {
            getUserInputAfterSimulation()
          }

          else -> println("Invalid input provided. Only 1 or 2 is allowed\n")
        }
      }
      println()
    }
  }

  private fun getUserInputAfterSimulation(): ConsoleCommand =
    parseConsoleInput {
      println()
      println(
        """
        Please choose from the following options:
        [1] Start over
        [2] Exit
        """.trimIndent(),
      )
      print("Your input: ")
      when (readlnOrNull()) {
        "1" -> ConsoleCommand.START_OVER
        "2" -> ConsoleCommand.EXIT
        else -> throw InvalidInputException("Invalid input provided. Only 1 or 2 is allowed\n")
      }
    }

  private fun runSimulation(
    field: Field,
    operations: List<Operation>,
  ) {
    if (operations.isEmpty()) {
      println("No cars provided! Unable to run simulation")
      throw IllegalStateException("No cars provided! Unable to run simulation")
    }
    val simulationResult = ControlCentre(field, operations).runSimulation()
    printListOfCars(operations)
    println("\nAfter simulation, the result is:")
    simulationResult
      .map {
        when {
          it.collisionInfo != null -> {
            val carsCollided =
              it.collisionInfo.collidedCars.joinToString { cc ->
                "${cc.name} at (${cc.coordinate.x},${cc.coordinate.y})"
              }
            "${it.car.name}, collides with $carsCollided at step ${it.collisionInfo.step}"
          }

          else -> "${it.car.name}, (${it.car.coordinate.x}, ${it.car.coordinate.y}), ${it.car.direction.name}"
        }
      }.onEach { println("- $it") }
  }

  private fun addNewOperation(operations: List<Operation>): List<Operation> =
    (operations + inputCarDetails()).also {
      printListOfCars(it)
    }

  private fun printListOfCars(operations: List<Operation>) {
    println("\nYour current list of cars are: ")
    operations.forEach { (car, commands) ->
      val carInformation =
        listOfNotNull(
          car.name,
          "(${car.coordinate.x}, ${car.coordinate.y})",
          car.direction.name,
          commands
            .joinToString(
              "",
            ).ifEmpty { null },
        )
      println(
        "- ${carInformation.joinToString()}",
      )
    }
  }

  private fun inputCarDetails(): Operation {
    val name =
      parseConsoleInput {
        print("\nPlease enter the name of the car: ")
        val name = readlnOrNull()
        require(!name.isNullOrBlank()) { "Car name is mandatory" }
        name
      }

    val car =
      parseConsoleInput {
        print("Please enter initial position of car $name in x y Direction format: ")
        val (x, y, direction) =
          readlnOrNull()?.split(' ')
            ?: throw InvalidInputException("Invalid x y Direction format provided!")
        Car(name, Coordinate(x.toLong(), y.toLong()), Direction.valueOf(direction.uppercase()))
      }

    val commands =
      parseConsoleInput {
        print("Please enter the commands for car $name: ")
        readlnOrNull()?.let {
          ArrayDeque(it.chunked(1).map { op -> Command.valueOf(op) })
        } ?: throw InvalidInputException("Invalid operation/s were provided!")
      }

    return Operation(car, commands)
  }
}

private fun <T> parseConsoleInput(invoke: () -> T): T {
  while (true) {
    runCatching {
      invoke()
    }.onSuccess {
      return it
    }.onFailure {
      println(it)
    }
  }
}
