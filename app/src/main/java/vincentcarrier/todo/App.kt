package vincentcarrier.todo

import android.app.Application
import android.content.Context
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import com.github.salomonbrys.kodein.singleton
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import io.reactivex.plugins.RxJavaPlugins
import vincentcarrier.todo.data.databaseModule
import vincentcarrier.todo.data.models.MyObjectBox
import vincentcarrier.todo.data.remote.webServiceModule
import vincentcarrier.todo.screens.projectsModule
import vincentcarrier.todo.screens.tasksModule


class App : Application(), KodeinAware {

  companion object {
    lateinit var instance: App
      private set
  }

  lateinit var boxStore: BoxStore
    private set

  override fun onCreate() {
    super.onCreate()
    instance = this
    boxStore = MyObjectBox.builder().androidContext(this).build()
    RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    if (BuildConfig.DEBUG) AndroidObjectBrowser(boxStore).start(this)
  }

  override val kodein by Kodein.lazy {
    bind<App>() with singleton { this@App }
    bind<Context>() with singleton { this@App }
    bind<BoxStore>() with singleton { instance<App>().boxStore }
    import(webServiceModule)
    import(databaseModule)
    import(projectsModule)
    import(tasksModule)
  }
}