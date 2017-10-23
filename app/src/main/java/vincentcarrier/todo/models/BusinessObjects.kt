package vincentcarrier.todo.models

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import org.joda.time.DateTime

@Entity
class Project() { // ObjectBox needs either an empty or all-args constructor
  constructor(name: String) : this() {
    this.name = name
  }

  @Id var id: Long = 0
  lateinit var name: String
  @Backlink lateinit var tasks: ToMany<Task>
}

@Entity
class Task() {
  constructor(name: String) : this() {
    this.name = name
  }

  @Id var id: Long = 0
  lateinit var name: String
  @Convert(converter = JodaDateTimeConverter::class, dbType = Long::class)
  val dateCreated: DateTime = DateTime()
  lateinit var project: ToOne<Project>
}

class JodaDateTimeConverter : PropertyConverter<DateTime, Long> {
  override fun convertToEntityProperty(long: Long?): DateTime? = long?.let { DateTime(long) }
  override fun convertToDatabaseValue(dateTime: DateTime?): Long? = dateTime?.millis
}