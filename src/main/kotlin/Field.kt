package com.zuhlke

data class Field(
  val x: Int,
  val y: Int,
) {
  val grid: Array<LongArray> by lazy {
    runCatching {
      Array(x) { LongArray(y) }
    }.getOrElse {
      throw InvalidInputException("Only positive values are allowed for field size.")
    }
  }
}
