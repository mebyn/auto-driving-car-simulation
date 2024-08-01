package simulator.console

import com.zuhlke.main
import com.zuhlke.simulator.console.InvalidInputException
import com.zuhlke.simulator.console.SimulatorConsole
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream

class SimulatorConsoleTest {
  @Test
  fun `should perform a simulation for one car and exit successfully`() {
    val input =
      """
      10 10
      1
      A
      1 2 N
      FFRFFFFRRL
      2
      2
      """.trimIndent()
    System.setIn(ByteArrayInputStream(input.toByteArray()))
    main()
  }

  @Test
  fun `should perform a simulation for 2 cars and exit successfully`() {
    val input =
      """
      10 10
      1
      A
      1 2 N
      FFRFFFFRRL
      1
      B
      7 8 W
      FFLFFFFFFF
      2
      2
      """.trimIndent()
    System.setIn(ByteArrayInputStream(input.toByteArray()))
    main()
  }

  @Test
  fun `should perform a simulation for 3 cars and exit successfully`() {
    val input =
      """
      10 10
      1
      A
      1 2 N
      FFRFFFFRRL
      1
      B
      7 8 W
      FFLFFFFFFF
      1
      C
      4 9 N
      RFRFFFFFFF
      2
      2
      """.trimIndent()
    System.setIn(ByteArrayInputStream(input.toByteArray()))
    main()
  }

  @Test
  fun `should throw exception when user provides an invalid input for x y Direction`() {
    val exception =
      assertThrows<InvalidInputException> {
        SimulatorConsole.parseUserForInputCarDetail("10 10")
      }
    assertThat(exception.message).isEqualTo("Invalid input. Input should be in x y Direction format")
  }

  @Test
  fun `should return field has been created message`() {
    val inputResult = SimulatorConsole.getFieldWidthAndHeightFromInput("10 10")
    assertThat(inputResult.result)
      .hasFieldOrPropertyWithValue("width", 10L)
      .hasFieldOrPropertyWithValue("height", 10L)
    assertThat(inputResult.message).isEqualTo("You have created a field of 10 x 10\n")
  }
}
