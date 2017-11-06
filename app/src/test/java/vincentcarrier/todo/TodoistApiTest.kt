package vincentcarrier.todo

import org.amshove.kluent.shouldNotBeEmpty
import org.junit.Test
import vincentcarrier.todo.data.remote.todoistApi


class TodoistApiTest {

//  val server = MockWebServer().apply { start() }
//  val baseUrl: HttpUrl = server.url("/api/v7/")
//
//
//  val service = Retrofit().createTodoistApi(baseUrl = baseUrl.toString())

  @Test
  fun `projects should load`() {
    val response = todoistApi.fetchProjects().blockingFirst()

    response.projects.shouldNotBeEmpty()
    response.tasks.shouldNotBeEmpty()
  }
}