package vincentcarrier.todo.models

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

interface BusinessEntity

@Entity
class Project() : BusinessEntity { // ObjectBox needs either an empty or all-args constructor
  constructor(name: String) : this() {
    this.name = name
  }

  constructor(response: ProjectJson) : this() {
    id = response.id
    name = response.name
  }

  @Id(assignable = true) var id: Long = 0
  lateinit var name: String
  @Backlink lateinit var tasks: ToMany<Task>
}

@Entity
class Task() : BusinessEntity {
  constructor(name: String) : this() {
    this.name = name
    dateCreated = LocalDateTime()
  }

  constructor(response: TaskJson) : this() {
    id = response.id
    name = response.content
    dateCreated = LocalDateTime.parse(response.date_added,
        DateTimeFormat.forPattern("EEE dd MMM yyyy HH:mm:ss Z"))
    project.targetId = response.project_id
  }

  @Id(assignable = true) var id: Long = 0
  lateinit var name: String
  @Convert(converter = JodaDateTimeConverter::class, dbType = Long::class)
  lateinit var dateCreated: LocalDateTime
  lateinit var project: ToOne<Project>
}

class JodaDateTimeConverter : PropertyConverter<LocalDateTime, Long> {
  override fun convertToEntityProperty(long: Long?) = long?.let { LocalDateTime(long) }
  override fun convertToDatabaseValue(dateTime: LocalDateTime?) = dateTime?.toDateTime()?.millis
}