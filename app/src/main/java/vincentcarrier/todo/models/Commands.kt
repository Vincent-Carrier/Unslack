package vincentcarrier.todo.models

import android.support.annotation.StringDef
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import vincentcarrier.todo.models.Commands.ITEM_ADD
import vincentcarrier.todo.models.Commands.PROJECT_ADD
import java.util.UUID
import kotlin.annotation.AnnotationRetention.SOURCE

object Commands {
  const val PROJECT_ADD = "project_add"
  const val ITEM_ADD = "item_add"
  const val ITEM_REMOVE = "item_remove"
}

@Retention(SOURCE)
@StringDef(PROJECT_ADD, ITEM_ADD)
annotation class CommandType

@Entity
class Command() {
  constructor(@CommandType type: String, entity: BusinessEntity) : this() {
    this.type = type
    when (entity) {
      is Task -> task.target = entity
      is Project -> project.target = entity
    }
  }

  @Id var id: Long = 0
  @CommandType lateinit var type: String

  // Either one, but not both. Unfortunately, ObjectBox doesn't support this yet
  lateinit var task: ToOne<Task>
  lateinit var project: ToOne<Project>
}

class CommandJson(command: Command) {
  val temp_id = UUID.randomUUID().toString()
  val uuid = UUID.randomUUID().toString()
  @CommandType var type = command.type
  val args: Map<String, Any>  = when(type) {
      Commands.PROJECT_ADD -> {
        val project = command.project.target
        TODO()
      }
      Commands.ITEM_ADD -> {
        val task = command.task.target
        mapOf("content" to task.name, "project_id" to task.project.targetId)
      }
      Commands.ITEM_REMOVE -> {
        val task = command.task.target
        TODO()
      }
      else -> throw Exception("Couldn't create CommandJson from Command")
  }
}