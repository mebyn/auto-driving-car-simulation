package simulator

import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.Field
import com.zuhlke.simulator.console.InvalidInputException
import com.zuhlke.simulator.initializeField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class FieldTest {
  @Test
  fun `should throw exception when input null is provided for field generation`() {
    assertThrows<InvalidInputException> {
      initializeField(null)
    }.let { exception ->
      assertThat(
        exception.message,
      ).contains(
        "Invalid input provided!" +
          " Only positive integers of x y format are allowed. Provided input [null]",
      )
    }
  }

  @Test
  fun `should throw exception when input is empty for field generation`() {
    assertThrows<InvalidInputException> {
      initializeField("")
    }.let { exception ->
      assertThat(exception.message).contains(
        "Invalid input provided!" +
          " Only positive integers of x y format are allowed. Provided input []",
      )
    }
  }

  @Test
  fun `should throw exception when input is a b`() {
    assertThrows<InvalidInputException> {
      initializeField("a b")
    }.let { exception ->
      assertThat(
        exception.message,
      ).contains(
        "Invalid input provided!" +
          " Only positive integers of x y format are allowed. Provided input [a b]",
      )
    }
  }

  @Test
  fun `should fail when input has negative size of -10 10`() {
    assertThrows<InvalidInputException> {
      initializeField("-10 10")
    }.let { exception ->
      assertThat(exception.message).contains(
        "Invalid input provided!" +
          " Only positive integers of x y format are allowed. Provided input [-10 10]",
      )
    }
  }

  @Test
  fun `should pass when input is 10 10`() {
    val field =
      initializeField("10 10")

    assertThat(field)
      .isNotNull
      .hasFieldOrPropertyWithValue("width", 10L)
      .hasFieldOrPropertyWithValue("height", 10L)
  }

  @Test
  fun `should return true if coordinate is inside the boundary`() {
    val field = Field(10, 10)
    assertThat(field.isCoordinateWithinBoundary(Coordinate(9, 9))).isTrue()
  }

  @Test
  fun `should return true if coordinate is within the boundary`() {
    val field = Field(10, 10)
    assertThat(field.isCoordinateWithinBoundary(Coordinate(10, 10))).isTrue()
  }

  @Test
  fun `should return false if coordinate is outside the boundary`() {
    val field = Field(10, 10)
    assertThat(field.isCoordinateWithinBoundary(Coordinate(11, 10))).isFalse()
  }

  @Test
  fun `should pass when input car coordinate is 10 0`() {
    assertThrows<InvalidInputException> {
      initializeField("10 0")
    }
  }

  @Test
  fun `should pass when input car coordinate is 0 0`() {
    assertThrows<InvalidInputException> {
      initializeField("0 0")
    }
  }
}
