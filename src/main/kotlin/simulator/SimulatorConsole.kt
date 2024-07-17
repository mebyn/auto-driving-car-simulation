package com.zuhlke.simulator

import com.zuhlke.Car
import com.zuhlke.initializeField

object SimulatorConsole {
  fun start() {
    println("Welcome to Auto Driving Car Simulation!\n")
    val field = createField()
    println("You have created a field of ${field.size} x ${field[0].size}\n")
    while (true) {
      println(
        """
        Please choose from the following options:
        [1] Add a car to field
        [2] Run simulation
        """.trimIndent(),
      )
      when (readlnOrNull()) {
        "1" -> println("One")
        "2" -> println("Two")
        else -> println("Invalid input provided. Only 1 or 2 is allowed\n")
      }
    }
  }

  private fun createField(): Array<Array<Car?>> {
    while (true) {
      print("Please enter the width and height of the simulation field in x y format: ")
      runCatching {
        initializeField(readlnOrNull())
      }.onSuccess {
        return it
      }.onFailure {
        println(it)
      }
    }
  }
}

class InvalidInputException(
  message: String,
) : Exception(message)
