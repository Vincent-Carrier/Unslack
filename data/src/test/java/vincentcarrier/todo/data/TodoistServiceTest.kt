package vincentcarrier.todo.data

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeNull
import org.junit.Test
import vincentcarrier.todo.data.remote.TodoistApi
import vincentcarrier.todo.data.remote.webServiceModule


class TodoistServiceTest {

  private val kodein = Kodein {
    import(webServiceModule)
  }

  private val todoistService: TodoistApi = kodein.instance<TodoistApi>()

  private val response = todoistService.fetchProjects().blockingFirst()

  @Test
  fun `projects should load`() {
    with(response) {
      projects.shouldNotBeEmpty()
      items.shouldNotBeEmpty()
    }
  }

  @Test
  fun `projects should contain inbox`() {
    with(response) {
      projects.find { it.name == "Inbox" }.shouldNotBeNull()
      projects.find { it.name == "Non-existing project" }.shouldBeNull()
    }
  }
}