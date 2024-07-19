package simulator.console

import com.zuhlke.main
import org.junit.jupiter.api.Test
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
}
