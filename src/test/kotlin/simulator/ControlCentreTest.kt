package simulator

import com.zuhlke.simulator.CarOperation
import com.zuhlke.simulator.Command.F
import com.zuhlke.simulator.Command.L
import com.zuhlke.simulator.Command.R
import com.zuhlke.simulator.ControlCentre
import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.Field
import com.zuhlke.simulator.vehicle.Car
import com.zuhlke.simulator.vehicle.Direction
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ControlCentreTest {
  @Test
  fun `should move one car to correct position with correct direction after command FFRFFFFRRL`() {
    val operations =
      listOf(
        CarOperation(
          Car("PRIUS", Coordinate(1, 2), Direction.N),
          ArrayDeque(
            listOf(F, F, R, F, F, F, F, R, R, L),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(10, 10),
        operations,
      ).runSimulation()
    assertThat(result).hasSize(1)
    result.first().let {
      assertThat(it.car.direction).isEqualTo(Direction.S)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
    }
  }

  @Test
  fun `should move one car to correct position with correct direction after command FFLFFFFFFF`() {
    val operations =
      listOf(
        CarOperation(
          Car("MUSTANG", Coordinate(7, 8), Direction.W),
          ArrayDeque(
            listOf(F, F, L, F, F, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(10, 10),
        operations,
      ).runSimulation()
    assertThat(result).hasSize(1)
    result.first().let {
      assertThat(it.car.direction).isEqualTo(Direction.S)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 1L)
    }
  }

  @Test
  fun `should stop moving the car and return last position if move coordinate is less than 0`() {
    val operations =
      listOf(
        CarOperation(
          Car("MUSTANG", Coordinate(3, 3), Direction.W),
          ArrayDeque(
            listOf(F, L, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(5, 5),
        operations,
      ).runSimulation()
    assertThat(result).hasSize(1)
    result.first().let {
      assertThat(it.car.direction).isEqualTo(Direction.S)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 2L)
        .hasFieldOrPropertyWithValue("y", 0L)
    }
  }

  @Test
  fun `should stop moving the car and return last position if move coordinate is greater than field size`() {
    val operations =
      listOf(
        CarOperation(
          Car("MUSTANG", Coordinate(3, 3), Direction.E),
          ArrayDeque(
            listOf(F, L, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(5, 5),
        operations,
      ).runSimulation()
    assertThat(result).hasSize(1)
    result.first().let {
      assertThat(it.car.direction).isEqualTo(Direction.N)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 4L)
        .hasFieldOrPropertyWithValue("y", 5L)
    }
  }

  @Test
  fun `should stop moving the car when two cars collide`() {
    val operations =
      listOf(
        CarOperation(
          Car("LAMBO", Coordinate(1, 2), Direction.N),
          ArrayDeque(
            listOf(F, F, R, F, F, F, F, R, R, L),
          ),
        ),
        CarOperation(
          Car("FERRARI", Coordinate(7, 8), Direction.W),
          ArrayDeque(
            listOf(F, F, L, F, F, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(10, 10),
        operations,
      ).runSimulation()
    assertThat(result).hasSize(2)
    result.find { it.car.name == "LAMBO" }.let {
      requireNotNull(it)
      assertThat(it.car.direction).isEqualTo(Direction.E)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.collidedCars)
          .contains(Car("FERRARI", Coordinate(5, 4), Direction.S))
        assertThat(collision.step).isEqualTo(7)
      }
    }
    result.find { it.car.name == "FERRARI" }.let {
      requireNotNull(it)
      assertThat(it.car.direction).isEqualTo(Direction.S)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.collidedCars)
          .contains(Car("LAMBO", Coordinate(5, 4), Direction.E))
        assertThat(collision.step).isEqualTo(7)
      }
    }
  }

  @Test
  fun `should stop moving the car when collision is detected at initial positions`() {
    val operations =
      listOf(
        CarOperation(
          Car("LAMBO", Coordinate(1, 2), Direction.N),
          ArrayDeque(
            listOf(F, F, R, F, F, F, F, R, R, L),
          ),
        ),
        CarOperation(
          Car("FERRARI", Coordinate(1, 2), Direction.W),
          ArrayDeque(
            listOf(F, F, L, F, F, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(10, 10),
        operations,
      ).runSimulation()
    assertThat(result).hasSize(2)
    result.find { it.car.name == "LAMBO" }.let {
      requireNotNull(it)
      assertThat(it.car.direction).isEqualTo(Direction.N)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 1L)
        .hasFieldOrPropertyWithValue("y", 2L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.collidedCars)
          .contains(Car("FERRARI", Coordinate(1, 2), Direction.W))
        assertThat(collision.step).isEqualTo(0)
      }
    }
    result.find { it.car.name == "FERRARI" }.let {
      requireNotNull(it)
      assertThat(it.car.direction).isEqualTo(Direction.W)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 1L)
        .hasFieldOrPropertyWithValue("y", 2L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(1)
        assertThat(collision.collidedCars)
          .contains(Car("LAMBO", Coordinate(1, 2), Direction.N))
        assertThat(collision.step).isEqualTo(0)
      }
    }
  }

  @Test
  fun `should stop moving the car when three cars collide`() {
    val operations =
      listOf(
        CarOperation(
          Car("LAMBO", Coordinate(1, 2), Direction.N),
          ArrayDeque(
            listOf(F, F, R, F, F, F, F, R, R, L),
          ),
        ),
        CarOperation(
          Car("FERRARI", Coordinate(7, 8), Direction.W),
          ArrayDeque(
            listOf(F, F, L, F, F, F, F, F, F, F),
          ),
        ),
        CarOperation(
          Car("BUGATTI", Coordinate(4, 9), Direction.N),
          ArrayDeque(
            listOf(R, F, R, F, F, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(10, 10),
        operations,
      ).runSimulation()
    assertThat(result).hasSize(3)
    result.find { it.car.name == "LAMBO" }.let {
      requireNotNull(it)
      assertThat(it.car.direction).isEqualTo(Direction.E)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(2)
        assertThat(collision.collidedCars)
          .contains(Car("FERRARI", Coordinate(5, 4), Direction.S))
        assertThat(collision.step).isEqualTo(8)
      }
    }
    result.find { it.car.name == "FERRARI" }.let {
      requireNotNull(it)
      assertThat(it.car.direction).isEqualTo(Direction.S)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(2)
        assertThat(collision.collidedCars)
          .contains(Car("LAMBO", Coordinate(5, 4), Direction.E))
        assertThat(collision.step).isEqualTo(8)
      }
    }
    result.find { it.car.name == "BUGATTI" }.let {
      requireNotNull(it)
      assertThat(it.car.direction).isEqualTo(Direction.S)
      assertThat(it.car.coordinate)
        .hasFieldOrPropertyWithValue("x", 5L)
        .hasFieldOrPropertyWithValue("y", 4L)
      requireNotNull(it.collisionInfo).let { collision ->
        assertThat(collision.collidedCars).hasSize(2)
        assertThat(collision.collidedCars)
          .contains(Car("LAMBO", Coordinate(5, 4), Direction.E))
        assertThat(collision.step).isEqualTo(8)
      }
    }
  }
}
