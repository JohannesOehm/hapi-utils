import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding

/**
 * For Enums like ObservationCategory, which cannot be set directly to the corresponding attribute
 * Caution: this cannot be used on regular enums
 */
inline fun <reified T : Enum<T>> T.toCoding(): Coding {
    val javaClass = T::class.java
    val code = javaClass.getDeclaredMethod("toCode").invoke(this) as String
    val system = javaClass.getDeclaredMethod("getSystem").invoke(this) as String
    val display = javaClass.getDeclaredMethod("getDisplay").invoke(this) as String
    return Coding(system, code, display)
}

/**
 * For Enums like ObservationCategory, which cannot be set directly to the corresponding attribute
 * Caution: this cannot be used on regular enums
 */
inline fun <reified T : Enum<T>> T.toCodeableConcept() = CodeableConcept(this.toCoding())


/**
 * Fake constructor to support multiple Codings for CodeableConcept
 */
fun CodeableConcept(coding: Coding, coding2: Coding, vararg codings: Coding): CodeableConcept {
    return CodeableConcept(coding).apply {
        addCoding(coding2)
        for (coding in codings) {
            addCoding(coding)
        }
    }
}
