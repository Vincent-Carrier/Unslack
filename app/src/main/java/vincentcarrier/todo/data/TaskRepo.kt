package vincentcarrier.todo.data

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.Task_

class TaskRepo(private val projectId: Long, private val service: TodoistService = TodoistService()) {

  private val taskBox = App.boxStore.boxFor(Task::class.java)

  fun whenTasksLoaded(): Observable<List<Task>> {
    return ReactiveNetwork.observeNetworkConnectivity(App.instance)
        .observeOn(Schedulers.io())
        .flatMap { connectivity ->
          if (connectivity.isAvailable) loadFromNetwork() else loadFromDisk()
        }
  }

  private fun loadFromDisk() = RxQuery.observable(taskBox.query().equal(Task_.projectId, projectId).build())
      .subscribeOn(Schedulers.io())

  private fun loadFromNetwork() = service.whenTasksLoaded()
      .observeOn(Schedulers.io())
      .map { response ->
        response.items.map { Task(it) }
            .filter { it.project.targetId == projectId }
      }

  fun put(task: Task) = taskBox.put(task.apply { project.targetId = projectId })

  fun remove(task: Task) = taskBox.remove(task)
}

