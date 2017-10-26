package vincentcarrier.todo.data

import io.reactivex.Observable
import vincentcarrier.todo.data.local.TaskDao
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Task

class TaskRepo(projectId: Long) {

  private val db: TaskDao = TaskDao(projectId)
  private val service: TodoistService = TodoistService()

  fun whenTasksLoaded(): Observable<List<Task>> {
    return db.whenTasksLoaded() /*ReactiveNetwork.checkInternetConnectivity()
        .flatMap { isOnline ->
          if (isOnline) service.whenTasksLoaded() else db.whenTasksLoaded()
        }*/
  }

  fun addTask(task: Task) = db.addTask(task)

  fun removeTask(id: Long) = db.removeTask(id)
}

