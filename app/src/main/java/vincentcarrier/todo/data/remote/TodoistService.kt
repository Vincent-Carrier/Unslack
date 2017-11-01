package vincentcarrier.todo.data.remote

import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query
import vincentcarrier.todo.models.CommandJson
import vincentcarrier.todo.models.SyncJson
import vincentcarrier.todo.models.User


private val todoistApi = Retrofit().createTodoistApi(User.accessToken)

class TodoistService(api: TodoistApi = todoistApi) : TodoistApi by api

interface TodoistApi {
  @POST("""sync?sync_token="*"&resource_types=["projects","items"]""")
  fun whenProjectsLoaded(@Query("commands") commands: List<CommandJson>): Observable<SyncJson>

  @POST("""sync?sync_token="*"&resource_types=["items"]""")
  fun whenTasksLoaded(@Query("commands") commands: List<CommandJson>): Observable<SyncJson>
}