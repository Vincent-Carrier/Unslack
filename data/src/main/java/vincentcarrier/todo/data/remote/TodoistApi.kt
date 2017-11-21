package vincentcarrier.todo.data.remote

import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query
import vincentcarrier.todo.data.models.CommandJson
import vincentcarrier.todo.data.models.SyncJson


interface TodoistApi {
  @POST("""sync?sync_token="*"&resource_types=["projects","items"]""")
  fun fetchProjects(@Query("commands") commands: List<CommandJson> = emptyList()): Observable<SyncJson>

  @POST("""sync?sync_token="*"&resource_types=["items"]""")
  fun fetchTasks(@Query("commands") commands: List<CommandJson> = emptyList()): Observable<SyncJson>
}