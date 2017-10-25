package vincentcarrier.todo.data

import io.reactivex.Single
import vincentcarrier.todo.data.local.TaskDatabase
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Task

class TaskRepository(projectId: Long) {

  private val db: TaskDatabase = TaskDatabase(projectId)
  private val service: TodoistService = TodoistService()

  fun whenTasksLoaded(): Single<List<Task>> {
    return service.whenTasksLoaded() /*ReactiveNetwork.checkInternetConnectivity()
        .flatMap { isOnline ->
          if (isOnline) service.whenTasksLoaded() else db.whenTasksLoaded()
        }*/
  }

  fun addTask(task: Task) = db.addTask(task)

  fun removeTask(id: Long) = db.removeTask(id)
}

