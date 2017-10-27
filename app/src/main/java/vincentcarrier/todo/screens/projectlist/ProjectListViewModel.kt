package vincentcarrier.todo.screens.projectlist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.airbnb.epoxy.TypedEpoxyController
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.startActivity
import vincentcarrier.todo.App
import vincentcarrier.todo.data.ProjectRepo
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.screens.PROJECT_ID
import vincentcarrier.todo.screens.tasklist.TaskListActivity


class ProjectListViewModel(app: Application, private val repo: ProjectRepo = ProjectRepo()) : AndroidViewModel(app) {

  private val controller = ProjectListController()

  internal val adapter = controller.adapter

  internal fun whenProjectsLoaded() = repo.whenProjectsLoaded()
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext { controller.setData(it) }

  inner class ProjectListController: TypedEpoxyController<List<Project>>() {
    override fun buildModels(Projects: List<Project>) {
      Projects.forEach { project ->
        projectItemView {
          id(project.id)
          name(project.name)
          openProject {
            getApplication<App>()
                .startActivity<TaskListActivity>(PROJECT_ID to project.id)
          }
        }
      }
    }
  }
}

class ProjectListVmFactory(private val app: Application, private val repo: ProjectRepo = ProjectRepo())
  : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST") return ProjectListViewModel(app, repo) as T
  }
}