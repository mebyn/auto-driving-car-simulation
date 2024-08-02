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
import java.io.BufferedReader

class SimulatorConsole(private val consoleInput: BufferedReader) {
  lateinit var field: Field

  fun start(): List<Car> {
    while (true) {
      println("Welcome to Auto Driving Car Simulation!\n")
      print("Please enter the width and height of the simulation field in x y format: ")
      field = getFieldWidthAndHeightFromInput()
      val simulationInputs = parseSimulationInputs(emptyList())
      val simulationResult = ControlCentre(field, simulationInputs).runSimulation()
      simulationInputs.printOutput()
      println("\nAfter simulation, the result is:")
      simulationResult.printSimulationReport()
      when (parseUserInputAfterSimulation()) {
        ConsoleCommand.START_OVER -> continue
        ConsoleCommand.EXIT -> {
          println("\nThank you for running the simulation. Goodbye!")
          return simulationResult
        }
      }
    }
  }

  private fun List<Car>.printSimulationReport() =
    map { result ->
      val collisionInfo = result.collisionInfo
      when {
        collisionInfo != null -> {
          val carsCollided =
            collisionInfo.collidedCars.joinToString { cc ->
              "${cc.name} at (${cc.coordinate.x},${cc.coordinate.y})"
            }
          "${result.name}, collides with $carsCollided at step ${collisionInfo.step}"
        }

        else -> "${result.name}, (${result.coordinate.x}, ${result.coordinate.y}), ${result.direction.name}"
      }
    }.onEach { println("- $it") }

  fun getFieldWidthAndHeightFromInput(): Field =
    parseConsoleInput {
      initializeField(consoleInput.readInputLine)
    }.let { field ->
      println("You have created a field of ${field.width} x ${field.height}\n")
      field
    }

  fun parseSimulationInputs(simulationInputs: List<SimulationInput>): List<SimulationInput> =
    parseConsoleInput {
      println(
        """
        Please choose from the following options:
        [1] Add a car to field
        [2] Run simulation
        """.trimIndent(),
      )
      print("Your input: ")
      when (consoleInput.readInputLine) {
        "1" -> parseSimulationInputs(addNewInput(simulationInputs))
        "2" -> {
          if (simulationInputs.isEmpty()) {
            println("No simulation entry provided! Unable to run simulation")
            throw IllegalStateException("No simulation entry provided! Unable to run simulation")
          }
          simulationInputs
        }

        else -> throw InvalidInputException("Invalid input provided. Only 1 or 2 is allowed\n")
      }
    }

  fun parseUserInputAfterSimulation(): ConsoleCommand =
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
      when (consoleInput.readInputLine) {
        "1" -> ConsoleCommand.START_OVER
        "2" -> ConsoleCommand.EXIT
        else -> throw InvalidInputException("Invalid input provided. Only 1 or 2 is allowed\n")
      }
    }

  private fun addNewInput(simulationInputs: List<SimulationInput>): List<SimulationInput> =
    (simulationInputs + inputCarDetails()).also {
      it.printOutput()
    }

  private fun List<SimulationInput>.printOutput() {
    println("\nYour current list of cars are: ")
    forEach { (car, commands) ->
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
    print("\nPlease enter the name of the car: ")
    val name = parseForCarName()

    print("Please enter initial position of car $name in x y Direction format: ")
    val orientation = parseForCarOrientation()

    print("Please enter the commands for car $name: ")
    val commands = parseForCarCommands()

    return SimulationInput(Car(name, orientation), commands)
  }

  fun parseForCarName(): String =
    parseConsoleInput {
      val name = consoleInput.readInputLine
      require(name.isNotBlank()) { "Car name is mandatory" }
      name
    }

  fun parseForCarCommands(): ArrayDeque<Command> =
    parseConsoleInput {
      val commands =
        runCatching {
          ArrayDeque(consoleInput.readInputLine.chunked(1).map { op -> Command.valueOf(op) })
        }.getOrElse {
          throw InvalidInputException("Invalid input. Only available commands are ${Command.entries.joinToString()}")
        }
      commands.ifEmpty { throw InvalidInputException("No command was provided!") }
    }

  fun parseForCarOrientation(): Orientation =
    parseConsoleInput {
      val (coordinate, direction) =
        runCatching {
          consoleInput.readInputLine.split(' ').let {
            val (x, y, direction) = it
            val coordinate = Coordinate(x.toLong(), y.toLong())
            when {
              field.isCoordinateOutOfBounds(coordinate) -> throw InvalidInputException()
              else -> coordinate to direction
            }
          }
        }.getOrElse {
          throw InvalidInputException("Invalid input. Input should be in x y Direction format and within field boundary")
        }
      Orientation(coordinate, Direction.valueOf(direction.uppercase()))
    }
}

private fun <T> parseConsoleInput(
  messagePrompt: () -> String = { "Please enter input again: " },
  invoke: () -> T,
): T {
  while (true) {
    runCatching {
      invoke()
    }.onSuccess {
      return it
    }.onFailure {
      println(it)
      print(messagePrompt())
    }
  }
}

val BufferedReader.readInputLine: String
  get() = readLine()
