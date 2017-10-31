package vincentcarrier.todo.data.local

import io.objectbox.Box
import io.objectbox.query.QueryBuilder
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.models.Command
import vincentcarrier.todo.models.Project
import vincentcarrier.todo.models.Task


class ProjectDao(override val box: Box<Project> = App.boxStore.boxFor(Project::class.java)) : Dao<Project>

class TaskDao(
    val projectId: Long? = null,
    override val box: Box<Task> = App.boxStore.boxFor(Task::class.java)
) : Dao<Task>

class CommandDao(override val box: Box<Command> = App.boxStore.boxFor(Command::class.java)) : Dao<Command>

interface Dao<T> { // Using Kotlin delegates would have been preferable, but threw a ClassCastException
  val box: Box<T>
  fun get(id: Long): T  = box.get(id)
  fun get(ids: Iterable<Long>): List<T> = box.get(ids)
  fun count(): Long = box.count()
  fun getAll(): List<T> = box.getAll()
  fun put(entity: T): Long = box.put(entity)
  fun put(entities: Collection<T>?) = box.put(entities)
  fun remove(id: Long) = box.remove(id)
  fun removeByKeys(ids: Collection<Long>?) = box.removeByKeys(ids)
  fun remove(`object`: T) = box.remove(`object`)
  fun remove(objects: Collection<T>?) = box.remove(objects)
  fun removeAll() = box.removeAll()

  fun whenLoaded(query: QueryBuilder<T> = box.query()): Observable<List<T>> {
    return RxQuery.observable(query.build())
        .subscribeOn(Schedulers.io())
  }
}