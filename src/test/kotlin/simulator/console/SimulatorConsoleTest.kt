package simulator.console

import com.zuhlke.simulator.Field
import com.zuhlke.simulator.console.SimulatorConsole
import com.zuhlke.simulator.console.readInputLine
import com.zuhlke.simulator.vehicle.Direction
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.BufferedReader

class SimulatorConsoleTest {
  @BeforeEach
  fun setup() {
    mockkStatic(BufferedReader::readInputLine)
  }

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
      """.trimIndent().toBufferedReader()
    val result = SimulatorConsole(input).start()
    verify(exactly = 7) { input.readInputLine }
    assert(result.size == 1)
    val car = result.first()
    assert(car.name == "A")
    assert(car.coordinate.x == 5L)
    assert(car.coordinate.y == 4L)
    assert(car.direction == Direction.S)
    assert(car.collisionInfo == null)
  }

  @Test
  fun `should perform a simulation for two cars with collision and exit successfully`() {
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
      """.trimIndent().toBufferedReader()
    val result = SimulatorConsole(input).start()
    verify(exactly = 11) { input.readInputLine }
    assert(result.size == 2)
    val car = result.first()
    assert(car.name == "A")
    assert(car.coordinate.x == 5L)
    assert(car.coordinate.y == 4L)
    assert(car.direction == Direction.E)
    assert(car.collisionInfo != null)
    assert(car.collisionInfo?.step == 7)
    assert(car.collisionInfo?.collidedCars?.size == 1)
  }

  @Test
  fun `should create field when width is 10 and height is 10`() {
    val result = SimulatorConsole("10 10".toBufferedReader()).getFieldWidthAndHeightFromInput()
    assert(result.height == 10L)
    assert(result.width == 10L)
  }

  @Test
  fun `should continuously prompt field input when invalid value is provided`() {
    val input =
      """
      A A
      10 -1
      -10 10
      10 10
      """.trimIndent().toBufferedReader()
    val result = SimulatorConsole(input).getFieldWidthAndHeightFromInput()
    verify(exactly = 4) { input.readInputLine }
    assert(result.height == 10L)
    assert(result.width == 10L)
  }

  @Test
  fun `should generate car name on valid input`() {
    val input =
      """
      MCLAREN
      """.trimIndent().toBufferedReader()
    val result = SimulatorConsole(input).parseForCarName()
    verify { input.readInputLine }
    assert(result == "MCLAREN")
  }

  @Test
  fun `should continuously prompt user input on invalid car name`() {
    val input =
      """

      TEST
      """.trimIndent().toBufferedReader()
    SimulatorConsole(input).parseForCarName()
    verify(exactly = 2) { input.readInputLine }
  }

  @Test
  fun `should create orientation valid x y Direction input (5 5 N)`() {
    val input =
      """
      5 5 N
      """.trimIndent().toBufferedReader()
    val console =
      SimulatorConsole(input).apply {
        field = Field(10, 10)
      }
    val result = console.parseForCarOrientation()
    verify { input.readInputLine }
    assert(result.coordinate.x == 5L)
    assert(result.coordinate.y == 5L)
    assert(result.direction == Direction.N)
  }

  @Test
  fun `should continuously prompt user input on invalid x y Direction`() {
    val input =
      """
      10 10
      -1 10 N
      10 10 A
      10 10 N
      """.trimIndent().toBufferedReader()
    SimulatorConsole(input).apply {
      field = Field(10, 10)
    }.parseForCarOrientation()
    verify(exactly = 4) { input.readInputLine }
  }

  @Test
  fun `should return correct car command on valid input`() {
    val input =
      """
      FFLRFLR
      """.trimIndent().toBufferedReader()
    val result = SimulatorConsole(input).parseForCarCommands()
    verify { input.readInputLine }
    assert(result.size == 7)
  }

  @Test
  fun `should continuously prompt user input on invalid car command`() {
    val input =
      """

      FFAFFLR
      FLFRFLO
      FFLRFLR
      """.trimIndent().toBufferedReader()
    SimulatorConsole(input).parseForCarCommands()
    verify(exactly = 4) { input.readInputLine }
  }

  @Test
  fun `should continuously prompt user input on invalid after simulation command`() {
    val input =
      """
      3
      A
      1
      """.trimIndent().toBufferedReader()
    SimulatorConsole(input).parseUserInputAfterSimulation()
    verify(exactly = 3) { input.readInputLine }
  }

  @Test
  fun `should continuously prompt for simulation input until simulation run command`() {
    val input =
      """
      1
      A
      10 10 N
      FFFFFF
      1
      B
      10 10 N
      FFFFFF
      2
      """.trimIndent().toBufferedReader()
    val inputs =
      SimulatorConsole(input).apply {
        field = Field(10, 10)
      }.parseSimulationInputs(emptyList())
    assertThat(inputs.size == 2)
  }

  @Test
  fun `should continuously prompt for simulation input if an invalid command is entered`() {
    val input =
      """
      1
      A
      10 10 N
      FFFFFF
      1
      B
      10 10 N
      FFFFFF
      3
      3
      2
      """.trimIndent().toBufferedReader()
    SimulatorConsole(input).apply {
      field = Field(10, 10)
    }.parseSimulationInputs(emptyList())
    verify(atMost = 11) { input.readInputLine }
  }

  @Test
  fun `should continuously prompt for simulation input if simulation is started with no entries`() {
    val input =
      """
      2
      2
      1
      A
      10 10 N
      FFFFFF
      2
      """.trimIndent().toBufferedReader()
    SimulatorConsole(input).apply {
      field = Field(10, 10)
    }.parseSimulationInputs(emptyList())
    verify(atMost = 7) { input.readInputLine }
  }
}

private fun String.toBufferedReader() = byteInputStream().bufferedReader()
