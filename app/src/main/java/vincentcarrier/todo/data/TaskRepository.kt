package vincentcarrier.todo.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.data.local.TaskDatabase
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Task


class TaskRepository(private val db: TaskDatabase = TaskDatabase(),
                     private val service: TodoistService = TodoistService()) {

  fun whenTasksLoaded(): Single<List<Task>> {
//    return ReactiveNetwork.checkInternetConnectivity()
//        .flatMap { isOnline ->
//          if (isOnline) service.whenTasksLoaded() else db.whenTasksLoaded()
//        }
    return db.whenTasksLoaded()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
  }

  fun addTask(task: Task) = db.addTask(task)
}