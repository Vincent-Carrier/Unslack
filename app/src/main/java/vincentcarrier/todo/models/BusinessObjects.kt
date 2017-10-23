package vincentcarrier.todo.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Project(
    @Id var id: Long = 0,
    var name: String
)

@Entity
data class Task(
    @Id var id: Long = 0,
    var name: String
)