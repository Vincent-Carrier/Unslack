package vincentcarrier.todo.data

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.App
import vincentcarrier.todo.data.local.CommandDao
import vincentcarrier.todo.data.local.Dao
import vincentcarrier.todo.data.remote.TodoistService
import vincentcarrier.todo.models.User
import java.util.concurrent.TimeUnit.SECONDS


abstract class Repo<T> {

  internal val service = TodoistService()
  abstract val dao: Dao<T>
  internal val commandDao = CommandDao()

  internal fun whenLoaded(): Observable<List<T>> =
      ReactiveNetwork.observeNetworkConnectivity(App.instance)
          .subscribeOn(Schedulers.io())
          .flatMap { connectivity ->
            if (connectivity.isAvailable and User.needsSyncing(10)) {
              Observable.interval(0, 10, SECONDS)
                  .flatMap { whenLoadedFromNetwork() }
            } else whenLoadedFromDisk()
          }

  open internal fun whenLoadedFromDisk(): Observable<List<T>> = dao.whenLoaded()

  abstract fun whenLoadedFromNetwork(): Observable<List<T>>
}

