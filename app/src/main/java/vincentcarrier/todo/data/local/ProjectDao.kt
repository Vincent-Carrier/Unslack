package vincentcarrier.todo.data.local

import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Project


class ProjectDao {

  private val projectBox: Box<Project> = App.boxStore.boxFor(Project::class.java)

  fun whenProjectsLoaded(): Observable<List<Project>> {
    return RxQuery.observable(projectBox.query().build())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
  }

  fun add(project: Project) = projectBox.put(project)

  init {
    if (projectBox.all.count() < 2) {
      add(Project("My main project"))
      add(Project("My side project"))
    }
  }
}