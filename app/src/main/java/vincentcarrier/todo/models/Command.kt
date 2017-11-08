package vincentcarrier.todo.models

import com.squareup.moshi.Json
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import vincentcarrier.todo.models.CommandType.ITEM_ADD
import vincentcarrier.todo.models.CommandType.ITEM_REMOVE
import vincentcarrier.todo.models.CommandType.PROJECT_ADD
import java.util.UUID

@Entity
class Command {
  @Id
  var id: Long = 0

  @Json(name = "type")
  @Convert(converter = CommandTypeConverter::class, dbType = Int::class)
  lateinit var type: CommandType

  lateinit var project: ToOne<Project>

  lateinit var task: ToOne<Task>

  // For ObjectBox
  constructor() {}

  constructor(type: CommandType, project: Project) {
    this.type = type
    this.project.target = project
  }

  constructor(type: CommandType, task: Task) {
    this.type = type
    this.task.target = task
  }
}

class CommandJson(command: Command) {
  val temp_id = UUID.randomUUID().toString()
  val uuid = UUID.randomUUID().toString()
  val type = command.type.name.toLowerCase()
  val args: Map<String, Any> = when(command.type) {
    PROJECT_ADD -> { val project = command.project.target
      mapOf(
          "name" to project.name,
          "id" to project.id
      )
    }

    ITEM_ADD, ITEM_REMOVE -> { val task = command.task.target
      mapOf(
          "content" to task.name,
          "project_id" to task.project.targetId
      )
    }
  }
}

enum class CommandType {
  PROJECT_ADD, ITEM_ADD, ITEM_REMOVE
}