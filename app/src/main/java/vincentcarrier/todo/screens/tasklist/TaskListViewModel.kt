package vincentcarrier.todo.screens.tasklist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.android.schedulers.AndroidSchedulers
import vincentcarrier.todo.data.TaskRepo
import vincentcarrier.todo.models.Task


class TaskListViewModel(private val repo: TaskRepo) : ViewModel() {

  internal fun whenTasksLoaded() =
      repo.whenLoaded()
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext {
        controller.setData(it)
      }

  internal fun addTask(name: String) = repo.put(Task(name))

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

class TaskListVmFactory(private val repo: TaskRepo)
  : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST") return TaskListViewModel(repo) as T
  }
}