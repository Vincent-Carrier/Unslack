package vincentcarrier.todo.models


data class SyncJson(
    val projects: List<ProjectJson>,
    val items: List<TaskJson>
)