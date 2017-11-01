package vincentcarrier.todo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import io.reactivex.plugins.RxJavaPlugins
import vincentcarrier.todo.models.MyObjectBox


class App : Application() {

  companion object {
    lateinit var instance: App
      private set
    lateinit var boxStore: BoxStore
      private set

    fun isOnline(): Boolean {
      val netInfo = (instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
      return netInfo != null && netInfo.isConnectedOrConnecting
    }
  }

  override fun onCreate() {
    super.onCreate()
    RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    instance = this
    boxStore = MyObjectBox.builder().androidContext(this).build()
    if (BuildConfig.DEBUG) AndroidObjectBrowser(boxStore).start(this)
  }
}