package com.zuhlke.simulator

import com.zuhlke.Car
import com.zuhlke.Coordinate
import com.zuhlke.Direction
import com.zuhlke.initializeField

object SimulatorConsole {
  fun start() {
    println("Welcome to Auto Driving Car Simulation!\n")
    val field =
      parseConsoleInput {
        print("Please enter the width and height of the simulation field in x y format: ")
        initializeField(readlnOrNull())
      }

    println("You have created a field of ${field.width} x ${field.height}\n")
    var garage = emptyList<CarEntry>()
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
          "1" -> garage = addCarToGarage(garage)
          "2" -> runSimulation(garage)
          else -> println("Invalid input provided. Only 1 or 2 is allowed\n")
        }
      }
      println()
    }
  }

  private fun addCarToGarage(garage: List<CarEntry>): List<CarEntry> {
    val newGarage = garage + inputCarDetails()
    println("\nYour current list of cars are: ")
    newGarage.forEach { (car, command) ->
      println(
        "- ${car.name}, (${car.coordinate.x}, ${car.coordinate.y}), ${car.direction.name}, ${
          command.joinToString(
            "",
          )
        } ",
      )
    }
    return newGarage
  }

  private fun runSimulation(garage: List<CarEntry>) {
    garage.forEach { (car, command) ->
      println(
        "Simulating - ${car.name}, (${car.coordinate.x}, ${car.coordinate.y}), ${car.direction.name}, ${
          command.joinToString(
            "",
          )
        } ",
      )
    }
  }

  private fun inputCarDetails(): CarEntry {
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
          ArrayDeque(it.chunked(1).map { op -> Operation.valueOf(op) })
        } ?: throw InvalidInputException("Invalid operation/s were provided!")
      }

    return CarEntry(car, commands)
  }
}

class InvalidInputException(
  message: String = "",
) : Exception(message)

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
