package vincentcarrier.todo.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.data.local.TaskDao
import vincentcarrier.todo.models.Command
import vincentcarrier.todo.models.Commands
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.Task_

class TaskRepo(private val projectId: Long) : Repo<Task>() {

  override val dao = TaskDao(projectId)

  override fun whenLoadedFromDisk() = dao.whenLoaded({ it.equal(Task_.projectId, projectId) })

  override fun whenLoadedFromNetwork(): Observable<List<Task>> {
    return service.whenTasksLoaded()
        .observeOn(Schedulers.io())
        .map { response ->
          response.items.map { Task(it) }
              .filter { it.project.targetId == projectId }
        }
  }

  fun put(task: Task) {
    commandDao.put(Command(Commands.ITEM_ADD, task))
    dao.put(task.apply { project.targetId = projectId })
  }

  fun remove(task: Task) {
    commandDao.put(Command(Commands.ITEM_REMOVE, task))
    dao.remove(task)
  }
}

