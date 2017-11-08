package vincentcarrier.todo.data.remote

import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query
import vincentcarrier.todo.models.CommandJson
import vincentcarrier.todo.models.SyncJson


internal abstract class TodoistApi {
  @POST("""sync?sync_token="*"&resource_types=["projects","tasks"]""")
  abstract fun fetchProjects(@Query("commands") commands: List<CommandJson> = emptyList()): Observable<SyncJson>

  @POST("""sync?sync_token="*"&resource_types=["tasks"]""")
  abstract fun fetchTasks(@Query("commands") commands: List<CommandJson> = emptyList()): Observable<SyncJson>
}