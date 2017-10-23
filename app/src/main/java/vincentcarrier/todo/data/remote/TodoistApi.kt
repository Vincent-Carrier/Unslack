package vincentcarrier.todo.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task


private val todoistApi = Retrofit().createTodoistApi("")

class TodoistService(api: TodoistApi = todoistApi) : TodoistApi by api

interface TodoistApi {
  @GET("sync")
  fun whenProjectsLoaded(): Single<List<Project>>

  @GET("tasks")
  fun whenTasksLoaded(/*@Query("project_id") projectId: Int*/): Single<List<Task>>
}