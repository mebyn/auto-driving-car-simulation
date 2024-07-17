package com.zuhlke

fun initializeField(input: String?) =
  runCatching {
    val (x, y) = input!!.split(' ').map(String::toInt)
    Array(x) { IntArray(y) }
  }.getOrElse {
    throw InvalidInputException(
      "Invalid input provided!" +
        " Only positive integers of x y format are allowed." +
        " Provided input [$input]",
    )
  }
