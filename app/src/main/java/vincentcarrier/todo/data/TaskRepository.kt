package vincentcarrier.todo.data

import io.reactivex.Single
import vincentcarrier.todo.data.local.TaskDatabase
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Task

// Note: Normally, one would abstract the database library from the Repository, however
// I wanted to cut down on the boilerplate
class TaskRepository(private val projectId: Long) {

  private val db: TaskDatabase = TaskDatabase(projectId)
  private val service: TodoistService = TodoistService()

  fun whenTasksLoaded(): Single<List<Task>> {
    return db.whenTasksLoaded()
  }

  fun addTask(task: Task) = db.addTask(task)

  fun removeTask(id: Long) = db.removeTask(id)
}

