package vincentcarrier.todo.models

import com.squareup.moshi.Json
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
class Project {
  @Id(assignable = true)
  @Json(name = "id")
  var id: Long = 0

  @Json(name = "name")
  lateinit var name: String

  @Backlink lateinit var tasks: ToMany<Task>

  // For ObjectBox
  constructor() {}

  // For user
  constructor(name: String, tasks: ToMany<Task>) {
    this.name = name
    this.tasks = tasks
  }

  // For Moshi
  constructor(id: Long, name: String) {
    this.id = id
    this.name = name
  }
}
