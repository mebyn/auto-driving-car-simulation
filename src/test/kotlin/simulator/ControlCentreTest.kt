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
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ControlCentreTest {
  @Test
  fun `should remove car from field once command is empty`() {
    val car = spyk(Car("BUGATTI", Coordinate(0, 0), Direction.N))
    val initialGarageState =
      listOf(
        CarEntry(
          car,
          ArrayDeque(
            listOf(F, F, L, R, R),
          ),
        ),
      )
    val result =
      ControlCentre(
        Field(10, 10),
        initialGarageState,
      ).runSimulation()
    assertThat(result).isEmpty()
    verify(atLeast = 5) { car.move(any()) }
  }
}
