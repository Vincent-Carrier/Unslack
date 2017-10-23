package vincentcarrier.todo.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Project(
    var name: String,
    @Id var id: Long = 0
)

@Entity
data class Task(
    var name: String,
    @Id var id: Long = 0
)