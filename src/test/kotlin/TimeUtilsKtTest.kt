import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.Test

internal class TimeUtilsKtTest {
    @Test
    fun testTimeConversion() {
        val time = LocalTime.of(12, 10)
        println(time)
        val fhirTime = time.toFhir()
        println(fhirTime)
        println(fhirTime.toLocalTime())
//        println(TimeType("12:10:00").toLocalTime())
        assertEquals(time, fhirTime.toLocalTime())
    }

    @Test
    fun testDateTimeConversion() {
        val javaDateTime = LocalDateTime.of(2022, 1, 1, 12, 12, 12)
        println(javaDateTime)
        val fhirDateTime = javaDateTime.toFhir()
        println(fhirDateTime)
        assertEquals(javaDateTime, fhirDateTime.toLocalDateTime())
    }

    @Test
    fun testDateConversion() {
        val javaDate = LocalDate.of(2022, 1, 1)
        val fhirDate = javaDate.toFhir()
        assertEquals(javaDate, fhirDate.toLocalDate())
    }
}