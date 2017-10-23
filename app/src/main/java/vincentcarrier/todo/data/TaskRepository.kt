package vincentcarrier.todo.data

import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Task


class TaskRepository(private val taskBox: Box<Task> = App.boxStore.boxFor(Task::class.java),
                     private val service: TodoistService = TodoistService()) {

  fun whenTasksLoaded(): Single<List<Task>> {
    return RxQuery.single(taskBox.query().build())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
  }

  fun addTask(task: Task) = taskBox.put(task)

  fun removeTask(id: Long) = taskBox.remove(id)
}