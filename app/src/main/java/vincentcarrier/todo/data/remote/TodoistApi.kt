package vincentcarrier.todo.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task


interface TodoistApi {
  @GET("sync")
  fun getAllProjects(): Single<List<Project>>

  @GET("tasks")
  fun getTasks(/*@Query("project_id") projectId: Int*/): Single<List<Task>>
}