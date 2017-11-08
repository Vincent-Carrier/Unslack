package vincentcarrier.todo.models


internal data class SyncJson(
    val projects: List<ProjectJson>,
    val items: List<TaskJson>
)