package com.zuhlke

data class Car(
  val name: String,
  val coordinate: Coordinate,
  val direction: Direction,
)

enum class Direction {
  N,
  S,
  E,
  W,
}
