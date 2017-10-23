package vincentcarrier.todo.data.remote

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import vincentcarrier.todo.models.Task

private val todoistApi = Retrofit().createTodoistApi("")

class TodoistService(private val api: TodoistApi = todoistApi)  {
  fun whenTasksLoaded(): Single<List<Task>> {
    return api.getTasks()
        .observeOn(AndroidSchedulers.mainThread())
  }
}