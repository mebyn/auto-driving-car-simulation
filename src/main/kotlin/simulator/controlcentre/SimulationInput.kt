package com.zuhlke.simulator.controlcentre

import com.zuhlke.simulator.vehicle.Car

data class SimulationInput(
  val car: Car,
  val commands: ArrayDeque<Command>,
)
