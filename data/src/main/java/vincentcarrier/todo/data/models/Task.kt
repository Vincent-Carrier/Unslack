package vincentcarrier.todo.data.models

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

@Entity
class Task {
  @Id(assignable = true)
  var id: Long = 0

  lateinit var name: String

  @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
  lateinit var dateAdded: LocalDateTime

  lateinit var project: ToOne<Project>

  // For ObjectBox
  constructor()

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
    this.dateAdded = LocalDateTime.parse(json.date_added,
        DateTimeFormat.forPattern("EEE dd MMM yyyy HH:mm:ss Z"))
    this.project.targetId = json.project_id
  }
}

data class TaskJson internal constructor(
    val id: Long,
    val content: String,
    val date_added: String,
    val project_id: Long
) {

  internal constructor(task: Task) : this(
      task.id,
      task.name,
      task.dateAdded.toString("EEE dd MMM yyyy HH:mm:ss Z"),
      task.project.targetId
  )
}