package com.zuhlke.simulator.vehicle

import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.controlcentre.Command

data class Car(
  val name: String,
  val coordinate: Coordinate,
  val direction: Direction,
) {
  fun move(command: Command): Car =
    when (direction) {
      Direction.N -> {
        when (command) {
          Command.F -> copy(coordinate = coordinate.copy(y = coordinate.y + 1))
          Command.R -> copy(direction = Direction.E)
          Command.L -> copy(direction = Direction.W)
        }
      }
      Direction.S -> {
        when (command) {
          Command.F -> copy(coordinate = coordinate.copy(y = coordinate.y - 1))
          Command.R -> copy(direction = Direction.W)
          Command.L -> copy(direction = Direction.E)
        }
      }
      Direction.E -> {
        when (command) {
          Command.F -> copy(coordinate = coordinate.copy(x = coordinate.x + 1))
          Command.R -> copy(direction = Direction.S)
          Command.L -> copy(direction = Direction.N)
        }
      }
      Direction.W -> {
        when (command) {
          Command.F -> copy(coordinate = coordinate.copy(x = coordinate.x - 1))
          Command.R -> copy(direction = Direction.N)
          Command.L -> copy(direction = Direction.S)
        }
      }
    }
}
