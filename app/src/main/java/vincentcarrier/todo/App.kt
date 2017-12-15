package vincentcarrier.todo

import android.app.Application
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import com.github.salomonbrys.kodein.singleton
import vincentcarrier.todo.data.databaseModule
import vincentcarrier.todo.data.models.MyObjectBox
import vincentcarrier.todo.data.webServiceModule
import vincentcarrier.todo.screens.projectsModule
import vincentcarrier.todo.screens.tasksModule


class App : Application(), KodeinAware {

  override fun onCreate() {
    super.onCreate()
//    if (BuildConfig.DEBUG) AndroidObjectBrowser(boxStore).start(this)
  }

  override val kodein by Kodein.lazy {
    bind<App>() with singleton { this@App }
    bind<Application>() with singleton { this@App }
    bind() from singleton { MyObjectBox.builder().androidContext(instance<App>()).build() }
    import(webServiceModule)
    import(databaseModule)
    import(projectsModule)
    import(tasksModule)
  }
}