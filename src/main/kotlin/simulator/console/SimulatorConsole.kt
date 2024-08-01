package com.zuhlke.simulator.console

import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.Field
import com.zuhlke.simulator.controlcentre.Command
import com.zuhlke.simulator.controlcentre.ControlCentre
import com.zuhlke.simulator.controlcentre.SimulationInput
import com.zuhlke.simulator.initializeField
import com.zuhlke.simulator.vehicle.Car
import com.zuhlke.simulator.vehicle.Direction
import com.zuhlke.simulator.vehicle.Orientation

data class ConsoleInputResult<T>(
  val result: T,
  val message: String,
)

object SimulatorConsole {
  fun start() {
    while (true) {
      println("Welcome to Auto Driving Car Simulation!\n")
      val inputField = getFieldWidthAndHeightFromInput(readlnOrNull())
      when (simulate(inputField.result)) {
        ConsoleCommand.START_OVER -> continue
        ConsoleCommand.EXIT -> {
          println("\nThank you for running the simulation. Goodbye!")
          return
        }
      }
    }
  }

  fun getFieldWidthAndHeightFromInput(input: String?): ConsoleInputResult<Field> {
    val field =
      parseConsoleInput {
        print("Please enter the width and height of the simulation field in x y format: ")
        initializeField(input)
      }
    return ConsoleInputResult(
      field,
      "You have created a field of ${field.width} x ${field.height}\n"
    )
  }

  private fun simulate(field: Field): ConsoleCommand {
    var carStates = emptyList<SimulationInput>()
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
          "1" -> carStates = addNewCar(carStates)
          "2" -> return runSimulation(field, carStates).let {
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
    simulationInputs: List<SimulationInput>,
  ) {
    if (simulationInputs.isEmpty()) {
      println("No cars provided! Unable to run simulation")
      throw IllegalStateException("No cars provided! Unable to run simulation")
    }
    val simulationResult = ControlCentre(field, simulationInputs).runSimulation()
    printListOfCars(simulationInputs)
    println("\nAfter simulation, the result is:")
    simulationResult
      .map {
        when {
          it.collisionInfo != null -> {
            val carsCollided =
              it.collisionInfo.collidedCars.joinToString { cc ->
                "${cc.name} at (${cc.coordinate.x},${cc.coordinate.y})"
              }
            "${it.name}, collides with $carsCollided at step ${it.collisionInfo.step}"
          }

          else -> "${it.name}, (${it.coordinate.x}, ${it.coordinate.y}), ${it.direction.name}"
        }
      }.onEach { println("- $it") }
  }

  private fun addNewCar(carStates: List<SimulationInput>): List<SimulationInput> =
    (carStates + inputCarDetails()).also {
      printListOfCars(it)
    }

  private fun printListOfCars(carStates: List<SimulationInput>) {
    println("\nYour current list of cars are: ")
    carStates.forEach { (car, commands) ->
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

  private fun inputCarDetails(): SimulationInput {
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
        val (x, y, direction) = parseUserForInputCarDetail(readlnOrNull())
        Car(name, Orientation(Coordinate(x.toLong(), y.toLong()), Direction.valueOf(direction.uppercase())))
      }

    val commands =
      parseConsoleInput {
        print("Please enter the commands for car $name: ")
        val commands =
          readlnOrNull()?.let {
            ArrayDeque(it.chunked(1).map { op -> Command.valueOf(op) })
          } ?: throw InvalidInputException("Invalid operation/s were provided!")
        commands.ifEmpty { throw InvalidInputException("No command was provided!") }
      }

    return SimulationInput(car, commands)
  }

  fun parseUserForInputCarDetail(input: String?): Triple<String, String, String> =
    runCatching {
      input?.split(' ')?.let {
        val (x, y, direction) = it
        Triple(x, y, direction)
      } ?: throw InvalidInputException("Invalid x y Direction format provided!")
    }.getOrElse {
      throw InvalidInputException("Invalid input. Input should be in x y Direction format")
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
