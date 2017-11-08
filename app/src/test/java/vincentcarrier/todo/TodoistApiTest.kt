package vincentcarrier.todo

import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeNull
import org.junit.Test
import vincentcarrier.todo.data.remote.todoistService


class TodoistApiTest {


  private val response = todoistService.fetchProjects().blockingFirst()

  @Test fun `projects should load`() {
    with(response) {
      projects.shouldNotBeEmpty()
      items.shouldNotBeEmpty()
    }
  }

  @Test fun `projects should contain inbox`() {
    with(response) {
      projects.find { it.name == "Inbox" }.shouldNotBeNull()
      projects.find { it.name == "Non-existing project" }.shouldBeNull()
    }
  }
}