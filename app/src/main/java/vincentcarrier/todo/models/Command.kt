package vincentcarrier.todo.models

import com.squareup.moshi.Json
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class Command {
  @Id @Json(name = "id")
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

enum class CommandType {
  PROJECT_ADD, ITEM_ADD, ITEM_REMOVE
}