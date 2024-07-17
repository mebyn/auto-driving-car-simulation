import com.zuhlke.InvalidInputException
import com.zuhlke.initializeField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class FieldCreationTest {

  @Test
  fun `should throw exception when input is null`() {
    val exception = assertThrows<InvalidInputException> {
      initializeField(null)
    }
    assertThat(exception.message).matches("Invalid input provided. Value is null")
  }

  @Test
  fun `should throw exception when input is empty`() {
    val exception = assertThrows<InvalidInputException> {
      initializeField("")
    }
    assertThat(exception.message).matches("Invalid input provided. Value is ")
  }

  @Test
  fun `should throw exception when input is a b`() {
    val exception = assertThrows<InvalidInputException> {
      initializeField("a b")
    }
    assertThat(exception.message).matches("Invalid input provided. Value is a b")
  }

  @Test
  fun `should pass when input is 10 10`() {
    val field = initializeField("10 10")
    assertThat(field.grid).isNotEmpty.hasSameDimensionsAs(Array(10) { LongArray(10) })
  }
}
