package com.zuhlke.simulator

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
)

data class Coordinate(
  val x: Long,
  val y: Long,
)
