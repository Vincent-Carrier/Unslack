package vincentcarrier.todo.data

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.CommandJson
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task


class ProjectRepo(private val service: TodoistService = TodoistService()) {

  private val projectBox: Box<Project> = App.boxStore.boxFor(Project::class.java)
  private val taskBox: Box<Task> = App.boxStore.boxFor(Task::class.java)
  private val commandBox = App.boxStore.boxFor(CommandJson::class.java)

  fun whenProjectsLoaded(): Observable<List<Project>> =
      ReactiveNetwork.observeNetworkConnectivity(App.instance)
      .observeOn(Schedulers.io())
      .flatMap { connectivity ->
        if (connectivity.isAvailable) loadFromNetwork() else loadFromDisk()
      }

  private fun loadFromNetwork() =
      service.whenProjectsLoaded()
      .map { response ->
        response.projects.map { Project(it) } to response.items.map { Task(it) }
      }
      .doOnNext { response ->
        projectBox.put(response.first)
        taskBox.put(response.second)
      }
      .map { response ->
        response.first
      }

  private fun loadFromDisk() =
      RxQuery.observable(projectBox.query().build())
      .subscribeOn(Schedulers.io())
}