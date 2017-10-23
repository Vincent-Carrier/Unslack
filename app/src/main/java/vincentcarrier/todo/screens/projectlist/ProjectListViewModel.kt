package vincentcarrier.todo.screens.projectlist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.Single
import org.jetbrains.anko.startActivity
import vincentcarrier.todo.App
import vincentcarrier.todo.data.ProjectRepository
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.screens.tasklist.TaskListActivity


class ProjectListViewModel(app: Application, private val repo: ProjectRepository = ProjectRepository()) : AndroidViewModel(app) {

  internal fun whenProjectsLoaded(): Single<List<Project>> {
    return repo.whenProjectsLoaded()
        .doOnSuccess { controller.setData(it) }
  }

  private val controller = ProjectListController()
  internal val adapter = controller.adapter

  inner class ProjectListController: TypedEpoxyController<List<Project>>() {
    override fun buildModels(Projects: List<Project>) {
      Projects.forEach { project ->
        projectItemView {
          id(project.id)
          name(project.name)
          openProject {
            getApplication<App>()
                .startActivity<TaskListActivity>("project_id" to project.id)
          }
        }
      }
    }
  }
}

class ProjectListVmFactory(private val app: Application, private val repo: ProjectRepository = ProjectRepository())
  : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST") return ProjectListViewModel(app, repo) as T
  }
}