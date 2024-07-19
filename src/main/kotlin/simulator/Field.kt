package com.zuhlke.simulator

import com.zuhlke.simulator.console.InvalidInputException

fun initializeField(input: String?): Field =
  runCatching {
    val (width, height) = input!!.split(' ').map(String::toLong)
    if (width < 0 || height < 0) throw InvalidInputException()
    Field(width, height)
  }.getOrElse {
    throw InvalidInputException(
      "Invalid input provided!" +
        " Only positive integers of x y format are allowed." +
        " Provided input [$input]\n",
    )
  }

data class Field(
  val width: Long,
  val height: Long,
) {
  fun isCoordinateWithinBoundary(coordinate: Coordinate) = isCoordinateOutOfBounds(coordinate).not()

  private fun isCoordinateOutOfBounds(coordinate: Coordinate) =
    coordinate.x <= 0 ||
      coordinate.x >= width ||
      coordinate.y <= 0 ||
      coordinate.y >= height
}

data class Coordinate(
  val x: Long,
  val y: Long,
)
