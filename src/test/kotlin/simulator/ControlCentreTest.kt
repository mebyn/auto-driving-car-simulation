package simulator

import com.zuhlke.simulator.Car
import com.zuhlke.simulator.CarEntry
import com.zuhlke.simulator.ControlCentre
import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.Direction
import com.zuhlke.simulator.Field
import com.zuhlke.simulator.Operation.F
import com.zuhlke.simulator.Operation.L
import com.zuhlke.simulator.Operation.R
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ControlCentreTest {
  @Test
  fun `should move one car to correct position with correct direction after command FFRFFFFRRL`() {
    val initialGarageState =
      listOf(
        CarEntry(
          Car("PRIUS", Coordinate(1, 2), Direction.N),
          ArrayDeque(
            listOf(F, F, R, F, F, F, F, R, R, L),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(10, 10),
        initialGarageState,
      ).runSimulation()
    assertThat(result).hasSize(1)
    val priusCar = result.find { it.name == "PRIUS" }
    assertThat(priusCar).isNotNull
    requireNotNull(priusCar)
    assertThat(priusCar.direction).isEqualTo(Direction.S)
    assertThat(priusCar.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 4L)
  }

  @Test
  fun `should move one car to correct position with correct direction after command FFLFFFFFFF`() {
    val initialGarageState =
      listOf(
        CarEntry(
          Car("MUSTANG", Coordinate(7, 8), Direction.W),
          ArrayDeque(
            listOf(F, F, L, F, F, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(10, 10),
        initialGarageState,
      ).runSimulation()
    assertThat(result).hasSize(1)
    val priusCar = result.find { it.name == "MUSTANG" }
    assertThat(priusCar).isNotNull
    requireNotNull(priusCar)
    assertThat(priusCar.direction).isEqualTo(Direction.S)
    assertThat(priusCar.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 1L)
  }

  @Test
  fun `should stop moving the car and return last position if move coordinate is less than 0`() {
    val initialGarageState =
      listOf(
        CarEntry(
          Car("MUSTANG", Coordinate(3, 3), Direction.W),
          ArrayDeque(
            listOf(F, L, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(5, 5),
        initialGarageState,
      ).runSimulation()
    assertThat(result).hasSize(1)
    val priusCar = result.find { it.name == "MUSTANG" }
    assertThat(priusCar).isNotNull
    requireNotNull(priusCar)
    assertThat(priusCar.direction).isEqualTo(Direction.S)
    assertThat(priusCar.coordinate)
      .hasFieldOrPropertyWithValue("x", 2L)
      .hasFieldOrPropertyWithValue("y", 0L)
  }

  @Test
  fun `should stop moving the car and return last position if move coordinate is greater than field size`() {
    val initialGarageState =
      listOf(
        CarEntry(
          Car("MUSTANG", Coordinate(3, 3), Direction.E),
          ArrayDeque(
            listOf(F, L, F, F, F, F, F),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(5, 5),
        initialGarageState,
      ).runSimulation()
    assertThat(result).hasSize(1)
    val priusCar = result.find { it.name == "MUSTANG" }
    assertThat(priusCar).isNotNull
    requireNotNull(priusCar)
    assertThat(priusCar.direction).isEqualTo(Direction.N)
    assertThat(priusCar.coordinate)
      .hasFieldOrPropertyWithValue("x", 4L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }

  @Test
  fun `should stop moving the car when collision is detected`() {
  }
}
