package vincentcarrier.todo.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.SyncResponse
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.User


private val todoistApi = Retrofit().createTodoistApi(User.accessToken)

class TodoistService(private val api: TodoistApi = todoistApi) {
  fun whenProjectsLoaded(): Single<List<Project>> {
    return api.whenProjectsLoaded()
        .map(SyncResponse::projects)
        .map( { list ->
          list.map { response ->
            Project(response) } })
  }

  fun whenTasksLoaded(): Single<List<Task>> {
    return api.whenTasksLoaded()
  }
}

interface TodoistApi {
  @GET("""sync?sync_token="*"&resource_types=["projects"]""")
  fun whenProjectsLoaded(): Single<SyncResponse>

  @GET("tasks")
  fun whenTasksLoaded(/*@Query("project_id") projectId: Int*/): Single<List<Task>>
}