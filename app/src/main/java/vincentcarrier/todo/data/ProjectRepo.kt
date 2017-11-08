package vincentcarrier.todo.data

import io.objectbox.kotlin.boxFor
import io.reactivex.Observable
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.User


class ProjectRepo : Repo<Project>(App.boxStore.boxFor<Project>()) {

  private val taskDao = App.boxStore.boxFor<Task>()

  override fun loadFromDisk(): Observable<List<Project>> = dao.observable()

  override fun loadFromNetwork(): Observable<List<Project>> {
    return service.fetchProjects()
        .map { it.projects.map { Project(it) } to it.items.map { Task(it) } }
        .doOnNext { response ->
          // Maybe not the most efficient, but certainly the most readable
          dao.removeAll()
          dao.put(response.first)
          taskDao.put(response.second)

          User.syncCompleted()
        }
        .map { response ->
          response.first
        }
  }
}