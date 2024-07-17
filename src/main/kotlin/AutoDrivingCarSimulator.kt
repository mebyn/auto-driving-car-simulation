package com.zuhlke

fun main() {
  println("Welcome to Auto Driving Car Simulation!")
  print("Please enter the width and height of the simulation field in x y format: ")
  val field = initializeField(readLine())
  println("You have created a field of ${field.x} x ${field.y}")
}

fun initializeField(input: String?): Field {
  val (x, y) = runCatching {
    input!!.split(' ').map(String::toInt)
  }.getOrElse {
    throw InvalidInputException("Invalid input provided. Value is $input")
  }
  return Field(x, y)
}

class InvalidInputException(message: String? = "") : Exception(message)
