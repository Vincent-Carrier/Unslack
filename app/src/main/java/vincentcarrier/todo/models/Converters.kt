package vincentcarrier.todo.models

import io.objectbox.converter.PropertyConverter
import org.joda.time.LocalDateTime


class LocalDateTimeConverter : PropertyConverter<LocalDateTime, Long> {
  override fun convertToEntityProperty(long: Long?) = long?.let { LocalDateTime(long) }
  override fun convertToDatabaseValue(dateTime: LocalDateTime?) = dateTime?.toDateTime()?.millis
}

class CommandTypeConverter : PropertyConverter<CommandType, Int> {
  override fun convertToEntityProperty(int: Int?): CommandType? = int?.let { CommandType.values()[int] }
  override fun convertToDatabaseValue(type: CommandType?): Int? = type?.ordinal
}