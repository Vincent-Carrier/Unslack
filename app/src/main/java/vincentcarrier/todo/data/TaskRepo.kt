package vincentcarrier.todo.data

import io.objectbox.kotlin.boxFor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Command
import vincentcarrier.todo.models.CommandType
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.Task_

class TaskRepo(private val projectId: Long) : Repo<Task>(App.boxStore.boxFor<Task>()) {

  private val projectDao = App.boxStore.boxFor<Project>()
  private val project = projectDao.get(projectId)

  override fun loadFromDisk() = dao.observable { it.equal(Task_.projectId, projectId) }

  override fun loadFromNetwork(): Observable<List<Task>> {
    return service.fetchTasks()
        .observeOn(Schedulers.io())
        .doOnNext { commandDao.removeAll() }
        .map { response ->
              response.tasks.filter { it.project.targetId == projectId }
        }
  }

  fun put(task: Task) {
    projectDao.put(project.apply { tasks.add(task) })
    if(!App.isOnline()) commandDao.put(Command(CommandType.ITEM_ADD, task))
  }

  fun put(name: String) {
    val task = Task(name, projectId)
    put(task)
  }

  fun remove(task: Task) {
    projectDao.put(project.apply { tasks.remove(task) })
    if(!App.isOnline()) commandDao.put(Command(CommandType.ITEM_REMOVE, task))
  }
}

