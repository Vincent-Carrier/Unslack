package vincentcarrier.todo.data.local

import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Task


class TaskDatabase(private val projectId: Long) {
  private val taskBox: Box<Task> = App.boxStore.boxFor(Task::class.java)

  fun whenTasksLoaded(): Single<List<Task>> {
    val query = taskBox.query().filter { task ->
      task.project.targetId == projectId
    }.build()
    return RxQuery.single(query)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .doOnSuccess { println(it) }
  }

  fun addTask(task: Task) = taskBox.put(task.apply { project.targetId = projectId })

  fun removeTask(id: Long) = taskBox.remove(id)
}