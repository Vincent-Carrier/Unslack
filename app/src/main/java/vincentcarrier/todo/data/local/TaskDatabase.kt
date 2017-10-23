package vincentcarrier.todo.data.local

import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Task


class TaskDatabase(private val taskBox: Box<Task> = App.boxStore.boxFor(Task::class.java)) {
  fun whenTasksLoaded(): Single<List<Task>> {
    return RxQuery.single(taskBox.query().build())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
  }

  fun addTask(task: Task) = taskBox.put(task)

  fun removeTask(id: Long) = taskBox.remove(id)
}