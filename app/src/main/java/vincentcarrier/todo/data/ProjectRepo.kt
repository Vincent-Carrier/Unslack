package vincentcarrier.todo.data

import io.reactivex.Observable
import org.joda.time.LocalDateTime
import vincentcarrier.todo.data.local.ProjectDao
import vincentcarrier.todo.data.local.TaskDao
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.User


class ProjectRepo : Repo<Project>() {

  override val dao = ProjectDao()
  private val taskDao = TaskDao()

  override fun whenLoadedFromNetwork(): Observable<List<Project>> {
    return service.whenProjectsLoaded()
        .map { response ->
          response.projects.map { Project(it) } to response.items.map { Task(it) }
        }
        .doOnNext { response ->
          dao.put(response.first)
          taskDao.put(response.second)

          User.lastSync = LocalDateTime()
        }
        .map { response ->
          response.first
        }
  }
}