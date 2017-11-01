package vincentcarrier.todo

import junit.framework.Assert.assertTrue
import org.junit.Test

class AppTest {

  val app = App()

  @Test
  fun `is online should be true`() {
    assertTrue(App.isOnline())
  }
}