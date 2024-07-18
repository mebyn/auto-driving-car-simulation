package simulator

import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.Field
import com.zuhlke.simulator.InvalidInputException
import com.zuhlke.simulator.initializeField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class FieldTest {
  @Test
  fun `should throw exception when input is null`() {
    val exception =
      assertThrows<InvalidInputException> {
        initializeField(null)
      }
    assertThat(
      exception.message,
    ).contains(
      "Invalid input provided!" +
        " Only positive integers of x y format are allowed. Provided input [null]",
    )
  }

  @Test
  fun `should throw exception when input is empty`() {
    val exception =
      assertThrows<InvalidInputException> {
        initializeField("")
      }
    assertThat(exception.message).contains(
      "Invalid input provided!" +
        " Only positive integers of x y format are allowed. Provided input []",
    )
  }

  @Test
  fun `should throw exception when input is a b`() {
    val exception =
      assertThrows<InvalidInputException> {
        initializeField("a b")
      }
    assertThat(
      exception.message,
    ).contains(
      "Invalid input provided!" +
        " Only positive integers of x y format are allowed. Provided input [a b]",
    )
  }

  @Test
  fun `should fail when input has negative size of -10 10`() {
    val exception =
      assertThrows<InvalidInputException> {
        initializeField("-10 10")
      }
    assertThat(
      exception.message,
    ).contains(
      "Invalid input provided!" +
        " Only positive integers of x y format are allowed. Provided input [-10 10]",
    )
  }

  @Test
  fun `should pass when input is 10 10`() {
    val field = initializeField("10 10")
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
  fun `should return false if coordinate is at the boundary`() {
    val field = Field(10, 10)
    assertThat(field.isCoordinateWithinBoundary(Coordinate(10, 10))).isFalse()
  }

  @Test
  fun `should return false if coordinate is within boundary`() {
    val field = Field(10, 10)
    assertThat(field.isCoordinateWithinBoundary(Coordinate(11, 10))).isFalse()
  }
}
