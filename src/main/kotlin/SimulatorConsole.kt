package com.zuhlke

object SimulatorConsole {
  fun start() {
    println("Welcome to Auto Driving Car Simulation!")
    val field = createField()
    println(field)
  }

  fun createField(): Array<IntArray> {
    while (true) {
      print("Please enter the width and height of the simulation field in x y format: ")
      runCatching {
        initializeField(readLine())
      }.onSuccess { field ->
        println("You have created a field of ${field.size} x ${field[0].size}")
        return field
      }.onFailure {
        println(it)
      }
    }
  }
}
