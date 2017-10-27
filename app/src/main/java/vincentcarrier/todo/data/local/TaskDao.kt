package vincentcarrier.todo.data.local

import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task
import vincentcarrier.todo.models.Task_


class TaskDao(private val projectId: Long) {

  private val projectBox = App.boxStore.boxFor(Project::class.java)
  private val taskBox = App.boxStore.boxFor(Task::class.java)

  private val project = projectBox.get(projectId)

  fun whenTasksLoaded(): Observable<List<Task>> {
    val query = taskBox.query().equal(Task_.projectId, projectId).build()
    return RxQuery.observable(query)
        .subscribeOn(Schedulers.io())
  }

  fun add(task: Task) = projectBox.put(project.apply { tasks.add(task) })

  fun remove(task: Task) = taskBox.remove(task)
}
