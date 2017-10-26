package vincentcarrier.todo.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.SyncResponse
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.User


private val todoistApi = Retrofit().createTodoistApi(User.accessToken)

class TodoistService(private val api: TodoistApi = todoistApi) {
  fun whenProjectsLoaded(): Observable<List<Project>> {
    return api.whenProjectsLoaded()
        .map(SyncResponse::projects)
        .map( { list ->
          list.map { response ->
            Project(response) } })
  }

  fun whenTasksLoaded(): Observable<List<Task>> {
    return api.whenTasksLoaded()
  }
}

interface TodoistApi {
  @GET("""sync?sync_token="*"&resource_types=["projects"]""")
  fun whenProjectsLoaded(): Observable<SyncResponse>

  @GET("tasks")
  fun whenTasksLoaded(/*@Query("project_id") projectId: Int*/): Observable<List<Task>>
}