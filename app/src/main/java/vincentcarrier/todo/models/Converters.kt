package vincentcarrier.todo.models

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import io.objectbox.converter.PropertyConverter
import org.joda.time.LocalDateTime


class LocalDateTimeConverter : PropertyConverter<LocalDateTime, Long> {
  @FromJson override fun convertToEntityProperty(long: Long?) = long?.let { LocalDateTime(long) }
  @ToJson override fun convertToDatabaseValue(dateTime: LocalDateTime?) = dateTime?.toDateTime()?.millis
}

class CommandTypeConverter : PropertyConverter<CommandType, Int> {
  @FromJson override fun convertToEntityProperty(int: Int?): CommandType? = int?.let { CommandType.values()[int] }
  @ToJson override fun convertToDatabaseValue(type: CommandType?): Int? = type?.ordinal
}

class CommandAdapter {
  @ToJson fun toJson(command: Command): CommandJson {
    return CommandJson(command)
  }
}