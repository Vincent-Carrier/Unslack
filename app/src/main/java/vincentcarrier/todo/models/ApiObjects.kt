package vincentcarrier.todo.models


class SyncResponse(
    val projects: List<ProjectResponse>,
    val items: List<TaskResponse>
)

class ProjectResponse(
    val id: Long,
    val name: String
)

class TaskResponse(
    val id: Long,
    val content: String,
    val project_id: Long,
    val date_added: String
)
