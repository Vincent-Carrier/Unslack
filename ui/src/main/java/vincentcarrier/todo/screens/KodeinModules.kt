package vincentcarrier.todo.screens

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.factory
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import vincentcarrier.todo.data.ProjectsRepo
import vincentcarrier.todo.data.TasksRepo
import vincentcarrier.todo.screens.projectlist.ProjectsActivity
import vincentcarrier.todo.screens.projectlist.ProjectsViewModel
import vincentcarrier.todo.screens.projectlist.ProjectsVmFactory
import vincentcarrier.todo.screens.tasklist.TasksActivity
import vincentcarrier.todo.screens.tasklist.TasksViewModel
import vincentcarrier.todo.screens.tasklist.TasksVmFactory


val projectsModule = Kodein.Module {
  bind<ProjectsViewModel>() with factory { activity: ProjectsActivity ->
    ViewModelProviders.of(activity, instance("ProjectsVm")).get(ProjectsViewModel::class.java)
  }
  bind<ViewModelProvider.Factory>("ProjectsVm") with provider { ProjectsVmFactory(instance(), instance()) }
  bind<ProjectsRepo>() with provider { ProjectsRepo(this) }
  bind() from factory { activity: AppCompatActivity ->
    AndroidLifecycleScopeProvider.from(activity)
  }
}

val tasksModule = Kodein.Module {
  bind<TasksViewModel>() with factory { activity: TasksActivity ->
    ViewModelProviders.of(activity, instance("TasksVm")).get(TasksViewModel::class.java)
  }
  bind<ViewModelProvider.Factory>("TasksVm") with provider {
    TasksVmFactory(instance())
  }
  bind<TasksRepo>() with factory { projectId: Long -> TasksRepo(projectId) }
  bind() from factory { activity: AppCompatActivity ->
    AndroidLifecycleScopeProvider.from(activity)
  }
}