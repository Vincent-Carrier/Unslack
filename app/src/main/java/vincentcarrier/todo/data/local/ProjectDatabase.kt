package vincentcarrier.todo.data.local

import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Project


class ProjectDatabase {

  private val projectBox: Box<Project> = App.boxStore.boxFor(Project::class.java)

  fun whenProjectsLoaded(): Single<List<Project>> {
    return RxQuery.single(projectBox.query().build())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
  }

  fun addProject(project: Project) = projectBox.put(project)

//  init {
//    if (projectBox.all.count() < 2) {
//      addProject(Project("Inbox"))
//      addProject(Project("My side project"))
//    }
//  }
}