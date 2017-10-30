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
  lateinit var task: ToOne<Task>
  lateinit var project: ToOne<Project>
}

sealed class CommandJson {
  val temp_id = UUID.randomUUID().toString()
  val uuid = UUID.randomUUID().toString()
  @CommandType abstract val type: String
  abstract val args: Any

  class ProjectAddCommand(project: Project) : CommandJson() {
    override val type = PROJECT_ADD
    override val args = ProjectJson(project)
  }

  class ItemAddCommand(task: Task) : CommandJson() {
    override val type = ITEM_ADD
    override val args = TaskJson(task)
  }
}