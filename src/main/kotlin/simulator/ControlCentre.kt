package com.zuhlke.simulator

import com.zuhlke.Car

class ControlCentre

data class CarEntry(
  val car: Car,
  val commands: ArrayDeque<Operation>,
)

enum class Operation {
  L, // Rotate car by 90 degrees left
  R, // Rotate car by 90 degrees right
  F, // Move car forward by 1 grid point
}
