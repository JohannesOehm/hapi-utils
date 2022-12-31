import org.hl7.fhir.r4.model.DateTimeType
import org.hl7.fhir.r4.model.IntegerType
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.test.Test

internal class ParametersTest {
    @Test
    fun testTimeConversion() {
        val parameters = Parameters(mapOf(
            "bool" to true,
            "string" to "test",
            "int" to 123,
            "bla" to DateTimeType(Date())
        ))

        val bool: Boolean? = parameters["bool"]
        assertEquals(true, bool)

        parameters["bool"] = false
        assertEquals(false, parameters.get<Boolean>("bool"))


        val string: String? = parameters["string"]
        assertEquals("test", string)
        parameters["string"] = "Hallo Welt"
        assertEquals("Hallo Welt", parameters.get<String>("string"))


        val int: Int? = parameters["int"]
        assertEquals(123, int)
        parameters["int"] = 321
        assertEquals(321, parameters.get<Int>("int"))
        assertEquals(321, parameters.get<IntegerType>("int")!!.value)



        val bla: DateTimeType? = parameters["bla"]
        assertEquals(LocalDateTime.now().year, bla!!.year)
    }


}