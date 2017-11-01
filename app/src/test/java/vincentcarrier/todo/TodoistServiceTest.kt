package vincentcarrier.todo

import org.junit.Assert.assertNotNull
import org.junit.Test
import vincentcarrier.todo.data.remote.TodoistService


class TodoistServiceTest {

  val service = TodoistService()

  @Test
  fun `projects should load`() {
    val response = service.whenProjectsLoaded(emptyList()).blockingFirst()

    assertNotNull(response.projects)
    assertNotNull(response.items)
  }
}