package vincentcarrier.todo.models

import com.squareup.moshi.Json
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient
import io.objectbox.relation.ToOne
import org.joda.time.LocalDateTime

@Entity
class Task {
  @Id(assignable = true)
  @Json(name = "id")
  var id: Long = 0

  @Json(name = "content")
  lateinit var name: String

  @Json(name = "date_added")
  @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
  lateinit var dateAdded: LocalDateTime

  @Transient
  @Json(name = "project_id")
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
  constructor(id: Long, name: String, dateAdded: LocalDateTime, projectId: Long) {
    this.id = id
    this.name = name
    this.dateAdded = dateAdded
    this.projectId = projectId
    this.project.targetId = projectId
  }
}