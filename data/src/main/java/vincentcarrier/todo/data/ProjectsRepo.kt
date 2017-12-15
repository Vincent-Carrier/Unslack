package vincentcarrier.todo.data

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import io.objectbox.Box
import io.reactivex.Observable
import vincentcarrier.todo.data.models.Project
import vincentcarrier.todo.data.models.Sync
import vincentcarrier.todo.data.models.Task
import vincentcarrier.todo.data.models.User


class ProjectsRepo(kodein: Kodein) : Repo<Project>(kodein) {

  private val projectDao: Box<Project> = instance()
  private val taskDao: Box<Task> = instance()

  override fun loadFromDisk(): Observable<List<Project>> = projectDao.observable()

  override fun loadFromNetwork(): Observable<List<Project>> {
    return service.fetchProjects()
        .map { Sync(it) }
        .doOnNext {
          // Maybe not the most efficient, but certainly the most readable
          projectDao.removeAll()
          projectDao.put(it.projects)
          taskDao.put(it.tasks)

          User.syncCompleted()
        }
        .map { it.projects }
  }
}