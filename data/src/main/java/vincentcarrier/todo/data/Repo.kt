package vincentcarrier.todo.data

import android.content.Context
import android.net.ConnectivityManager
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.instance
import io.objectbox.Box
import io.objectbox.query.QueryBuilder
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vincentcarrier.todo.data.models.Command
import vincentcarrier.todo.data.models.User
import vincentcarrier.todo.data.remote.TodoistApi
import java.util.concurrent.TimeUnit.SECONDS


abstract class Repo<T> internal constructor(override val kodein: Kodein) : KodeinAware {

  protected val commandDao: Box<Command> = instance()
  protected val service: TodoistApi = instance()
  protected val context: Context = instance()

  fun whenLoaded(): Observable<List<T>> {
    return ReactiveNetwork.observeNetworkConnectivity(context)
        .subscribeOn(Schedulers.io())
        .flatMap { connectivity ->
          if (connectivity.isAvailable and User.needsSyncing()) {
            Observable.interval(0, 10, SECONDS)
                .flatMap { loadFromNetwork().mergeWith { loadFromDisk() }.distinct() }
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

fun Context.isOnline(): Boolean {
  val netInfo = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
  return netInfo != null && netInfo.isConnectedOrConnecting
}