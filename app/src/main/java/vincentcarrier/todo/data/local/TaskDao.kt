package vincentcarrier.todo.data.local

import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Task


class TaskDao(private val projectId: Long) {

  private val taskBox: Box<Task> = App.boxStore.boxFor(Task::class.java)

  fun whenTasksLoaded(): Observable<List<Task>> {
    val query = taskBox.query().filter { task ->
      task.project.targetId == projectId
    }.build()
    return RxQuery.observable(query)
        .subscribeOn(Schedulers.io())
  }

  fun addTask(task: Task) = taskBox.put(task.apply { project.targetId = projectId })

  fun removeTask(id: Long) = taskBox.remove(id)
}
