import com.zuhlke.initializeField
import com.zuhlke.simulator.InvalidInputException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class FieldCreationTest {
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
    assertThat(field).isNotEmpty.hasSameDimensionsAs(Array(10) { LongArray(10) })
  }
}
