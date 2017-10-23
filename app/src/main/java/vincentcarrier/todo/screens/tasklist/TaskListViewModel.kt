package vincentcarrier.todo.screens.tasklist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.Single
import vincentcarrier.todo.data.TaskRepository
import vincentcarrier.todo.models.Task


class TaskListViewModel(private val repo: TaskRepository = TaskRepository()) : ViewModel() {

  init {
    whenTasksLoaded().subscribe()
  }

  private fun whenTasksLoaded(): Single<List<Task>> {
    return repo.whenTasksLoaded()
        .doOnSuccess { controller.setData(it) }
  }

  internal fun addTask(name: String) {
    repo.addTask(Task(name = name))
    whenTasksLoaded().subscribe()
  }

  internal fun adapter() = controller.adapter

  private val controller = TaskListController()

  inner class TaskListController: TypedEpoxyController<List<Task>>() {
    override fun buildModels(tasks: List<Task>) {
      tasks.forEach { task ->
        taskItemView {
          id(task.id)
          name(task.name)
        }
      }
    }
  }
}

class TaskListVmFactory(private val repo: TaskRepository = TaskRepository())
  : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST") return TaskListViewModel(repo) as T
  }
}