package vincentcarrier.todo.data

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.data.local.CommandDao
import vincentcarrier.todo.data.local.TaskDao
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.Task_

class TaskRepo(private val projectId: Long, private val service: TodoistService = TodoistService()) {

  private val taskDao = TaskDao(projectId)
  private val commandDao = CommandDao()

  fun whenTasksLoaded(): Observable<List<Task>> =
      ReactiveNetwork.observeNetworkConnectivity(App.instance)
      .observeOn(Schedulers.io())
      .flatMap { connectivity ->
        if (connectivity.isAvailable) loadFromNetwork().mergeWith { loadFromDisk() }.distinct()
        else loadFromDisk()
      }

  private fun loadFromDisk() = taskDao.whenLoaded(taskDao.box.query().equal(Task_.projectId, projectId))

  private fun loadFromNetwork() =
      service.whenTasksLoaded()
      .observeOn(Schedulers.io())
      .map { response ->
        response.items.map { Task(it) }
            .filter { it.project.targetId == projectId }
      }

  fun put(task: Task) {
//    commandDao.put(Command(Commands.ITEM_ADD, task))
    taskDao.put(task.apply { project.targetId = projectId })
  }

  fun remove(task: Task) {
//    commandDao.put(Command(Commands.ITEM_REMOVE, task))
    taskDao.remove(task)
  }
}

