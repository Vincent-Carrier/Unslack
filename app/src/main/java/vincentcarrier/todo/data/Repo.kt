package vincentcarrier.todo.data

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import io.objectbox.query.QueryBuilder
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.data.remote.TodoistApi
import vincentcarrier.todo.data.remote.todoistApi
import vincentcarrier.todo.models.Command
import vincentcarrier.todo.models.User
import java.util.concurrent.TimeUnit.SECONDS


abstract class Repo<T>(
    protected val dao: Box<T>, // Type erasure is a bitch. Reified types cannot help us here.
    protected val commandDao: Box<Command> = App.boxStore.boxFor<Command>(),
    protected val service: TodoistApi = todoistApi
) {

  fun whenLoaded(): Observable<List<T>> {
    return ReactiveNetwork.observeNetworkConnectivity(App.instance)
        .subscribeOn(Schedulers.io())
        .flatMap { connectivity ->
          if (connectivity.isAvailable and User.needsSyncing()) {
            Observable.interval(0, 10, SECONDS)
                .flatMap { loadFromNetwork() }
          } else loadFromDisk()
        }
  }

  protected abstract fun loadFromDisk(): Observable<List<T>>

  protected abstract fun loadFromNetwork(): Observable<List<T>>
}

fun <T> Box<T>.observable(filter: (QueryBuilder<T>) -> QueryBuilder<T> = { it }): Observable<List<T>> {
  return RxQuery.observable(filter(query()).build())
      .subscribeOn(Schedulers.io())
}