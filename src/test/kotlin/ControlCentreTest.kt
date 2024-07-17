import com.zuhlke.Car
import com.zuhlke.Coordinate
import com.zuhlke.Direction
import com.zuhlke.simulator.ControlCentre
import com.zuhlke.simulator.InvalidFieldState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class ControlCentreTest {
  private val controlCentre = ControlCentre(Array(10) { arrayOfNulls(10) })

  @Test
  fun `should place the car correctly`() {
    val grid = controlCentre.placeCar(Car("lightning-mcqueen", Coordinate(5, 5), Direction.N))
    val car = grid[5][5]
    assertThat(car)
      .isNotNull
      .hasFieldOrPropertyWithValue("name", "lightning-mcqueen")
      .hasFieldOrPropertyWithValue("direction", Direction.N)
  }

  @Test
  fun `should throw exception when car placement position is out of range`() {
    val exception =
      assertThrows<InvalidFieldState> {
        controlCentre.placeCar(Car("incorrect-car", Coordinate(-5, 5), Direction.E))
      }
    assertThat(exception.message).contains("Invalid coordinate provided. Coordinate(x=-5, y=5)")
  }
}
