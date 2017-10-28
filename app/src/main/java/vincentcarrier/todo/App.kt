package vincentcarrier.todo

import android.app.Application
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
  }

  override fun onCreate() {
    super.onCreate()
    RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    instance = this
    boxStore = MyObjectBox.builder().androidContext(this).build()
    if (BuildConfig.DEBUG) AndroidObjectBrowser(boxStore).start(this)
  }
}