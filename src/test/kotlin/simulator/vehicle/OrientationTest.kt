package simulator.vehicle

import com.zuhlke.simulator.Coordinate
import com.zuhlke.simulator.controlcentre.Command
import com.zuhlke.simulator.vehicle.Direction
import com.zuhlke.simulator.vehicle.Orientation
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class OrientationTest {
  @Test
  fun `should move car forward north with F command when car direction is N`() {
    val orientation = Orientation(Coordinate(0, 0), Direction.N)
    val result = orientation.move(Command.F)
    assertThat(result.direction).isEqualTo(Direction.N)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 0L)
      .hasFieldOrPropertyWithValue("y", 1L)
  }

  @Test
  fun `should turn car facing west with L command when car direction is N`() {
    val orientation = Orientation(Coordinate(0, 0), Direction.N)
    val result = orientation.move(Command.L)
    assertThat(result.direction).isEqualTo(Direction.W)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 0L)
      .hasFieldOrPropertyWithValue("y", 0L)
  }

  @Test
  fun `should turn car facing east with R command when car direction is N`() {
    val orientation = Orientation(Coordinate(0, 0), Direction.N)
    val result = orientation.move(Command.R)
    assertThat(result.direction).isEqualTo(Direction.E)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 0L)
      .hasFieldOrPropertyWithValue("y", 0L)
  }

  @Test
  fun `should move car forward south with F command when car direction is S`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.S)
    val result = orientation.move(Command.F)
    assertThat(result.direction).isEqualTo(Direction.S)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 4L)
  }

  @Test
  fun `should turn car facing east with L command when car direction is S`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.S)
    val result = orientation.move(Command.L)
    assertThat(result.direction).isEqualTo(Direction.E)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }

  @Test
  fun `should turn car facing west with R command when car direction is S`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.S)
    val result = orientation.move(Command.R)
    assertThat(result.direction).isEqualTo(Direction.W)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }

  @Test
  fun `should move car forward east with F command when car direction is E`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.E)
    val result = orientation.move(Command.F)
    assertThat(result.direction).isEqualTo(Direction.E)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 6L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }

  @Test
  fun `should turn car facing north with L command when car direction is E`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.E)
    val result = orientation.move(Command.L)
    assertThat(result.direction).isEqualTo(Direction.N)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }

  @Test
  fun `should turn car facing south with R command when car direction is E`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.E)
    val result = orientation.move(Command.R)
    assertThat(result.direction).isEqualTo(Direction.S)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }

  @Test
  fun `should move car forward west with F command when car direction is W`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.W)
    val result = orientation.move(Command.F)
    assertThat(result.direction).isEqualTo(Direction.W)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 4L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }

  @Test
  fun `should turn car facing south with L command when car direction is W`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.W)
    val result = orientation.move(Command.L)
    assertThat(result.direction).isEqualTo(Direction.S)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }

  @Test
  fun `should turn car facing north with R command when car direction is W`() {
    val orientation = Orientation(Coordinate(5, 5), Direction.W)
    val result = orientation.move(Command.R)
    assertThat(result.direction).isEqualTo(Direction.N)
    assertThat(result.coordinate)
      .hasFieldOrPropertyWithValue("x", 5L)
      .hasFieldOrPropertyWithValue("y", 5L)
  }
}
