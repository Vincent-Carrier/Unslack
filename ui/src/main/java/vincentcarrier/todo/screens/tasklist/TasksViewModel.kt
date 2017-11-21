package vincentcarrier.todo.screens.tasklist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.android.schedulers.AndroidSchedulers
import junit.runner.Version.id
import vincentcarrier.todo.data.TasksRepo
import vincentcarrier.todo.data.models.Task
import vincentcarrier.todo.models.Task


class TasksViewModel(private val repo: TasksRepo) : ViewModel() {

  internal fun whenTasksLoaded() =
      repo.whenLoaded()
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext {
        controller.setData(it)
      }

  internal fun addTask(name: String) = repo.put(name)

  private val controller = TaskListController()
  internal val adapter = controller.adapter

  private inner class TaskListController: TypedEpoxyController<List<Task>>() {
    override fun buildModels(tasks: List<Task>) {
      tasks.forEach { task ->
        taskItemView {
          id(task.id)
          name(task.name)
          completeTask { repo.remove(task) }
        }
      }
    }
  }
}

class TasksVmFactory(private val repo: TasksRepo)
  : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST") return TasksViewModel(repo) as T
  }
}