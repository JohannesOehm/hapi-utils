import ca.uhn.fhir.model.api.TemporalPrecisionEnum
import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.DateType
import org.hl7.fhir.r4.model.TimeType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


fun LocalDateTime.toUtilDate(): Date = java.sql.Timestamp.valueOf(this)

fun LocalDateTime.toFhir(precision: TemporalPrecisionEnum = TemporalPrecisionEnum.SECOND) =
    DateTimeType(this.toUtilDate(), precision)

fun DateTimeType.toLocalDateTime() =
    LocalDateTime.of(this.year, this.month+1, this.day, this.hour, this.minute, this.second, this.millis*1000000)!!

fun LocalDate.toFhir() = DateType(this.year, this.monthValue-1, this.dayOfMonth)
fun DateType.toLocalDate() = LocalDate.of(this.year, this.month+1, this.day)!!

private val timeFormatter by lazy { DateTimeFormatter.ofPattern("HH:mm:ss[.SSSS]") }
fun LocalTime.toFhir() = TimeType(this.format(timeFormatter))
fun TimeType.toLocalTime() = LocalTime.parse(this.value, timeFormatter)!!

