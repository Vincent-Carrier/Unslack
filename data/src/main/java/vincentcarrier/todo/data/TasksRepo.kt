package vincentcarrier.todo.data

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import io.objectbox.Box
import io.objectbox.query.QueryBuilder
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.data.models.Command
import vincentcarrier.todo.data.models.CommandType.ITEM_ADD
import vincentcarrier.todo.data.models.CommandType.ITEM_REMOVE
import vincentcarrier.todo.data.models.Project
import vincentcarrier.todo.data.models.Task
import vincentcarrier.todo.data.models.Task_

class TasksRepo(private val projectId: Long, override val kodein: Kodein) : Repo<Task>() {

  private val taskDao: Box<Task> = instance()
  private val projectDao: Box<Project> = instance()
  private val project = projectDao.get(projectId)

  override fun loadFromDisk() = taskDao.observable { it.equal(Task_.projectId, projectId) }

  override fun loadFromNetwork(): Observable<List<Task>> {
    return service.fetchTasks()
        .observeOn(Schedulers.io())
        .doOnNext { commandDao.removeAll() }
        .map { response ->
              response.items
                  .map { Task(it) }
                  .filter { it.project.targetId == projectId }
        }
  }

  fun put(task: Task) {
    projectDao.put(project.apply { tasks.add(task) })
    if(!context.isOnline()) commandDao.put(Command(ITEM_ADD, task))
  }

  fun put(name: String) {
    val task = Task(name, projectId)
    put(task)
  }

  fun remove(task: Task) {
    projectDao.put(project.apply { tasks.remove(task) })
    if(!context.isOnline()) commandDao.put(
        Command(ITEM_REMOVE, task))
  }
}

fun <T> Box<T>.observable(filter: (QueryBuilder<T>) -> QueryBuilder<T> = { it }): Observable<List<T>> {
  return RxQuery.observable(filter(query()).build())
      .subscribeOn(Schedulers.io())
}