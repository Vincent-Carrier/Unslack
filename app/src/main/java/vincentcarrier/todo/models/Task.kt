package vincentcarrier.todo.models

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.joda.time.LocalDateTime

@Entity
class Task {
  @Id(assignable = true)
  var id: Long = 0

  lateinit var name: String

  @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
  lateinit var dateAdded: LocalDateTime

  private var projectId: Long = 0

  lateinit var project: ToOne<Project>

  // For ObjectBox
  constructor() {}

  // For user
  constructor(name: String, projectId: Long) {
    this.name = name
    this.dateAdded = LocalDateTime()
    this.project.targetId = projectId
  }

  // For Moshi
  internal constructor(json: TaskJson) {
    this.id = json.id
    this.name = json.content
    this.dateAdded = json.date_added
    this.projectId = projectId
    this.project.targetId = projectId
  }
}

internal data class TaskJson(
    val id: Long,
    val content: String,
    val date_added: String,
    val project_id: Long
)