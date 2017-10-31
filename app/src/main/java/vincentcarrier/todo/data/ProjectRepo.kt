package vincentcarrier.todo.data

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.data.local.CommandDao
import vincentcarrier.todo.data.local.ProjectDao
import vincentcarrier.todo.data.local.TaskDao
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task


class ProjectRepo(private val service: TodoistService = TodoistService()) {

  private val projectDao = ProjectDao()
  private val taskDao = TaskDao()
  private val commandDao = CommandDao()

  fun whenProjectsLoaded(): Observable<List<Project>> =
      ReactiveNetwork.observeNetworkConnectivity(App.instance)
      .observeOn(Schedulers.io())
      .flatMap { connectivity ->
        if (connectivity.isAvailable) loadFromNetwork() else projectDao.whenLoaded()
      }

  private fun loadFromNetwork() =
      service.whenProjectsLoaded()
      .map { response ->
        response.projects.map { Project(it) } to response.items.map { Task(it) }
      }
      .doOnNext { response ->
        projectDao.put(response.first)
        taskDao.put(response.second)
      }
      .map { response ->
        response.first
      }
}