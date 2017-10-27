package vincentcarrier.todo.data

import io.reactivex.Observable
import vincentcarrier.todo.data.local.ProjectDao
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Project


class ProjectRepo {

  private val db = ProjectDao()
  private val service = TodoistService()

  fun whenProjectsLoaded(): Observable<List<Project>> {
    return db.whenProjectsLoaded()
  }

  fun add(project: Project) = db.add(project)

}