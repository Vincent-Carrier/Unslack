package vincentcarrier.todo.data.models


data class SyncJson(
    val projects: List<ProjectJson>,
    val items: List<TaskJson>
)

internal class Sync(json: SyncJson) {
  val projects = json.projects.map { Project(it) }
  val tasks = json.items.map { Task(it) }
}