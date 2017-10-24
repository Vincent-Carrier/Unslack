package vincentcarrier.todo.data

import io.reactivex.Single
import vincentcarrier.todo.data.local.ProjectDatabase
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Project


class ProjectRepository {

  private val db = ProjectDatabase()
  private val service = TodoistService()

  fun whenProjectsLoaded(): Single<List<Project>> {
    return db.whenProjectsLoaded()
  }

  fun addProject(project: Project) = db.addProject(project)

}