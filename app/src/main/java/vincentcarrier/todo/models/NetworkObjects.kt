package vincentcarrier.todo.models


class SyncJson(
    val projects: List<ProjectJson>,
    val items: List<TaskJson>
)

class ProjectJson(
    val id: Long,
    val name: String
) {
  constructor(project: Project) : this(
      id = project.id,
      name = project.name
  )
}

class TaskJson(
    val id: Long,
    val content: String,
    val project_id: Long,
    val date_added: String
) {
  constructor(task: Task) : this(
      id = task.id,
      content = task.name,
      project_id = task.project.targetId,
      date_added = task.dateCreated.toString("EEE dd MMM yyyy HH:mm:ss Z")
  )
}