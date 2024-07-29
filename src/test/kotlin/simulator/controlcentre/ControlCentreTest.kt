package simulator.controlcentre

import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.Field
import com.zuhlke.simulator.controlcentre.Command
import com.zuhlke.simulator.controlcentre.ControlCentre
import com.zuhlke.simulator.controlcentre.SimulationInput
import com.zuhlke.simulator.vehicle.Car
import com.zuhlke.simulator.vehicle.Direction
import com.zuhlke.simulator.vehicle.Direction.E
import com.zuhlke.simulator.vehicle.Direction.N
import com.zuhlke.simulator.vehicle.Direction.S
import com.zuhlke.simulator.vehicle.Direction.W
import com.zuhlke.simulator.vehicle.Orientation
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ControlCentreTest {
  @Test
  fun `should move one car to correct position with correct direction after command FFRFFFFRRL`() {
    val result =
      runSimulation(
        10,
        10,
        TestSimulationInput("PRIUS", 1, 2, N, "FFRFFFFRRL"),
      )
    assertThat(result).hasSize(1)
    result.first().let {
      assertThat(it.direction).isEqualTo(S)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
    }
  }

  @Test
  fun `should move one car to correct position with correct direction after command FFLFFFFFFF`() {
    val result =
      runSimulation(
        10,
        10,
        TestSimulationInput("MUSTANG", 7, 8, W, "FFLFFFFFFF"),
      )
    assertThat(result).hasSize(1)
    result.first().let {
      assertThat(it.direction).isEqualTo(S)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 1L)
    }
  }

  @Test
  fun `should prevent car from moving if move coordinate is less than 0`() {
    val result =
      runSimulation(
        5,
        5,
        TestSimulationInput("PRIUS", 3, 3, W, "FLFFFFFLL"),
      )
    assertThat(result).hasSize(1)
    result.first().let {
      assertThat(it.direction).isEqualTo(N)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 2L)
        .hasFieldOrPropertyWithValue("y", 0L)
    }
  }

  @Test
  fun `should prevent car from moving if move coordinate is greater than field size`() {
    val result =
      runSimulation(
        5,
        5,
        TestSimulationInput("PRIUS", 3, 3, E, "FLFFFFFRRR"),
      )
    assertThat(result).hasSize(1)
    result.first().let {
      assertThat(it.direction).isEqualTo(W)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 4L)
        .hasFieldOrPropertyWithValue("y", 5L)
    }
  }

  @Test
  fun `should stop moving the car when two cars collide`() {
    val result =
      runSimulation(
        10,
        10,
        TestSimulationInput("LAMBO", 1, 2, N, "FFRFFFFRRL"),
        TestSimulationInput("FERRARI", 7, 8, W, "FFLFFFFFFF"),
      )
    assertThat(result).hasSize(2)
    result.find { it.name == "LAMBO" }.let {
      requireNotNull(it)
      assertThat(it.direction).isEqualTo(E)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.step).isEqualTo(7)
      }
    }
    result.find { it.name == "FERRARI" }.let {
      requireNotNull(it)
      assertThat(it.direction).isEqualTo(S)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.step).isEqualTo(7)
      }
    }
  }

  @Test
  fun `should stop moving the car when collision is detected at initial positions`() {
    val result =
      runSimulation(
        10,
        10,
        TestSimulationInput("LAMBO", 1, 2, N, "FFRFFFFRRL"),
        TestSimulationInput("FERRARI", 1, 2, W, "FFLFFFFFFF"),
      )
    assertThat(result).hasSize(2)
    result.find { it.name == "LAMBO" }.let {
      requireNotNull(it)
      assertThat(it.direction).isEqualTo(N)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 1L)
        .hasFieldOrPropertyWithValue("y", 2L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.step).isEqualTo(0)
      }
    }
    result.find { it.name == "FERRARI" }.let {
      requireNotNull(it)
      assertThat(it.direction).isEqualTo(W)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 1L)
        .hasFieldOrPropertyWithValue("y", 2L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.step).isEqualTo(0)
      }
    }
  }

  @Test
  fun `should stop moving the car when 3 cars collide`() {
    val result =
      runSimulation(
        10,
        10,
        TestSimulationInput("LAMBO", 1, 2, N, "FFRFFFFRRL"),
        TestSimulationInput("FERRARI", 7, 8, W, "FFLFFFFFFF"),
        TestSimulationInput("BUGATTI", 4, 9, N, "RFRFFFFFFF"),
      )
    assertThat(result).hasSize(3)
    result.find { it.name == "LAMBO" }.let {
      requireNotNull(it)
      assertThat(it.direction).isEqualTo(E)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(2)
        assertThat(collision.step).isEqualTo(8)
      }
    }
    result.find { it.name == "FERRARI" }.let {
      requireNotNull(it)
      assertThat(it.direction).isEqualTo(S)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(2)
        assertThat(collision.step).isEqualTo(8)
      }
    }
    result.find { it.name == "BUGATTI" }.let {
      requireNotNull(it)
      assertThat(it.direction).isEqualTo(S)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(2)
        assertThat(collision.step).isEqualTo(8)
      }
    }
  }

  @Test
  fun `should prompt and stop moving the car when collision is detected`() {
    val result =
      runSimulation(
        10,
        10,
        TestSimulationInput("A", 0, 0, E, "FFFFF"),
        TestSimulationInput("B", 2, 0, W, "FFFFF"),
      )
    assertThat(result).hasSize(2)
    result.find { it.name == "A" }.let {
      requireNotNull(it)
      assertThat(it.direction).isEqualTo(E)
      assertThat(it.coordinate)
        .hasFieldOrPropertyWithValue("x", 1L)
        .hasFieldOrPropertyWithValue("y", 0L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.step).isEqualTo(1)
      }
    }
  }

  data class TestSimulationInput(
    val name: String,
    val x: Long,
    val y: Long,
    val direction: Direction,
    val commands: String,
  )

  private fun runSimulation(
    fieldWidth: Long,
    fieldHeight: Long,
    vararg simulationInput: TestSimulationInput,
  ) = ControlCentre(
    Field(fieldWidth, fieldHeight),
    simulationInput.map {
      SimulationInput(
        Car(it.name, Orientation(Coordinate(it.x, it.y), it.direction)),
        ArrayDeque(it.commands.chunked(1).map { op -> Command.valueOf(op) }),
      )
    },
  ).runSimulation()
}
