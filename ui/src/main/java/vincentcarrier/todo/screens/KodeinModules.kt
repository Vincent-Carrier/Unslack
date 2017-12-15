package vincentcarrier.todo.screens

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.factory
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import vincentcarrier.todo.data.ProjectsRepo
import vincentcarrier.todo.data.TasksRepo
import vincentcarrier.todo.screens.projectlist.ProjectsViewModel
import vincentcarrier.todo.screens.projectlist.ProjectsVmFactory
import vincentcarrier.todo.screens.tasklist.TasksViewModel
import vincentcarrier.todo.screens.tasklist.TasksVmFactory


val projectsModule = Kodein.Module {
  bind<ProjectsViewModel>() with provider {
    ViewModelProviders.of(instance<AppCompatActivity>(), instance<ProjectsVmFactory>())
        .get(ProjectsViewModel::class.java)
  }

  bind<ProjectsVmFactory>() with provider { ProjectsVmFactory(instance(), instance()) }

  bind<ProjectsRepo>() with provider { ProjectsRepo(kodein) }
}

val tasksModule = Kodein.Module {
  bind<TasksViewModel>() with factory { projectId: Long ->
    // There seems to be no better way to deal with this, sadly
    ViewModelProviders.of(instance<AppCompatActivity>(), TasksVmFactory(TasksRepo(projectId, kodein)))
        .get(TasksViewModel::class.java)
  }
}