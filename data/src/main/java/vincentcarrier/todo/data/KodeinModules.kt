package vincentcarrier.todo.data

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.github.salomonbrys.kodein.singleton
import com.github.salomonbrys.kodein.with
import io.objectbox.Box
import io.objectbox.BoxStore
import vincentcarrier.todo.data.models.Command
import vincentcarrier.todo.data.models.Project
import vincentcarrier.todo.data.models.Task
import vincentcarrier.todo.data.models.User
import vincentcarrier.todo.data.remote.TodoistApi
import vincentcarrier.todo.data.remote.converterFactory
import vincentcarrier.todo.data.remote.okHttpClient
import vincentcarrier.todo.data.remote.retrofit
import vincentcarrier.todo.data.remote.service


val databaseModule = Kodein.Module {
  // There is probably a better way to do this
  bind<Box<Task>>() with provider { instance<BoxStore>().boxFor(Task::class.java) }
  bind<Box<Project>>() with provider { instance<BoxStore>().boxFor(Project::class.java) }
  bind<Box<Command>>() with provider { instance<BoxStore>().boxFor(Command::class.java) }
}

val webServiceModule = Kodein.Module {
  bind<TodoistApi>() with singleton { service(instance()) }
  bind() from singleton { retrofit(instance(), instance("base url"), instance()) }
  bind() from singleton { okHttpClient(User.accessToken) }
  bind() from singleton { converterFactory() }
  constant("base url") with "https://todoist.com/api/v7/"
}