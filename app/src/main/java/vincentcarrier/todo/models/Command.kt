package vincentcarrier.todo.models

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
  @Id var id: Long = 0

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
    PROJECT_ADD -> { val project = ProjectJson(command.project.target)
      mapOf(
          "id" to project.id,
          "name" to project.name
      )
    }

    ITEM_ADD, ITEM_REMOVE -> { val task = TaskJson(command.task.target)
      mapOf(
          "id" to task.id,
          "content" to task.content,
          "date_added" to task.date_added,
          "project_id" to task.project_id
      )
    }
  }
}

enum class CommandType {
  PROJECT_ADD, ITEM_ADD, ITEM_REMOVE
}