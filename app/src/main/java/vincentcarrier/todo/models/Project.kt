package vincentcarrier.todo.models

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
class Project {

  @Id(assignable = true)
  var id: Long = 0

  lateinit var name: String

  @Backlink lateinit var tasks: ToMany<Task>

  // For ObjectBox
  constructor() {}

  // For user
  constructor(name: String) {
    this.name = name
  }

  // For Moshi
  internal constructor(json: ProjectJson) {
    this.id = json.id
    this.name = json.name
  }
}

internal data class ProjectJson(
    val id: Long,
    val name: String
)