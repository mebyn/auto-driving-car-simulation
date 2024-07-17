package com.zuhlke

import com.zuhlke.simulator.InvalidInputException

fun initializeField(input: String?) =
  runCatching {
    val (x, y) = input!!.split(' ').map(String::toInt)
    Array(x) { arrayOfNulls<Car?>(y) }
  }.getOrElse {
    throw InvalidInputException(
      "Invalid input provided!" +
        " Only positive integers of x y format are allowed." +
        " Provided input [$input]\n",
    )
  }

data class Coordinate(
  val x: Int,
  val y: Int,
)
