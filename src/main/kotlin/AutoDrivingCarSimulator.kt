package com.zuhlke

fun main() {
  SimulatorConsole.start()
}

class InvalidInputException(message: String? = "") : Exception(message)
