package vincentcarrier.todo.screens.tasklist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.Single
import vincentcarrier.todo.data.TaskRepository
import vincentcarrier.todo.models.Task


class TaskListViewModel(private val repo: TaskRepository)
  : ViewModel() {

  internal fun whenTasksLoaded(): Single<List<Task>> {
    return repo.whenTasksLoaded()
        .doOnSuccess {
          controller.setData(it)
        }
  }

  internal fun addTask(name: String) {
    repo.addTask(Task(name))
    whenTasksLoaded().subscribe()
  }

  private val controller = TaskListController()
  internal val adapter = controller.adapter

  inner class TaskListController: TypedEpoxyController<List<Task>>() {
    override fun buildModels(tasks: List<Task>) {
      tasks.forEach { task ->
        taskItemView {
          id(task.id)
          name(task.name)
          completeTask {
            repo.removeTask(task.id)
            whenTasksLoaded().subscribe()
          }
        }
      }
    }
  }
}

class TaskListVmFactory(private val repo: TaskRepository)
  : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST") return TaskListViewModel(repo) as T
  }
}