package vincentcarrier.todo.models

import com.squareup.moshi.Json
import vincentcarrier.todo.models.CommandType.ITEM_ADD
import vincentcarrier.todo.models.CommandType.ITEM_REMOVE
import vincentcarrier.todo.models.CommandType.PROJECT_ADD
import java.util.UUID


data class SyncJson(
    @Json(name = "projects") val projects: List<Project>,
    @Json(name = "items") val tasks: List<Task>
)

class CommandJson(command: Command) {
  val temp_id = UUID.randomUUID().toString()
  val uuid = UUID.randomUUID().toString()
  val type = command.type.name.toLowerCase()
  val args: Map<String, Any>  = when(command.type) {
    PROJECT_ADD -> {
      val project = command.project.target
      mapOf(
          "name" to project.name,
          "id" to project.id
      )
    } ITEM_ADD, ITEM_REMOVE -> {
      val task = command.task.target
      mapOf(
          "content" to task.name,
          "project_id" to task.project.targetId
      )
    } else -> throw Exception("Couldn't create JSON from Command")
  }
}