package vincentcarrier.todo.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import vincentcarrier.todo.models.SyncResponse
import vincentcarrier.todo.models.User


private val todoistApi = Retrofit().createTodoistApi(User.accessToken)

class TodoistService(api: TodoistApi = todoistApi) : TodoistApi by api

interface TodoistApi {
  @GET("""sync?sync_token="*"&resource_types=["projects","items"]""")
  fun whenProjectsLoaded(): Observable<SyncResponse>

  @GET("""sync?sync_token="*"&resource_types=["items"]""")
  fun whenTasksLoaded(): Observable<SyncResponse>
}