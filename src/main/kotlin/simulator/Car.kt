package com.zuhlke.simulator

data class Car(
  val name: String,
  val coordinate: Coordinate,
  val direction: Direction,
) {
  fun move(command: Operation): Car =
    when (direction) {
      Direction.N -> {
        when (command) {
          Operation.F -> copy(coordinate = coordinate.copy(y = coordinate.y + 1))
          Operation.R -> copy(direction = Direction.E)
          Operation.L -> copy(direction = Direction.W)
        }
      }
      Direction.S -> {
        when (command) {
          Operation.F -> copy(coordinate = coordinate.copy(y = coordinate.y - 1))
          Operation.R -> copy(direction = Direction.W)
          Operation.L -> copy(direction = Direction.E)
        }
      }
      Direction.E -> {
        when (command) {
          Operation.F -> copy(coordinate = coordinate.copy(x = coordinate.x + 1))
          Operation.R -> copy(direction = Direction.S)
          Operation.L -> copy(direction = Direction.N)
        }
      }
      Direction.W -> {
        when (command) {
          Operation.F -> copy(coordinate = coordinate.copy(x = coordinate.x - 1))
          Operation.R -> copy(direction = Direction.N)
          Operation.L -> copy(direction = Direction.S)
        }
      }
    }
}

enum class Direction {
  N,
  S,
  E,
  W,
}
