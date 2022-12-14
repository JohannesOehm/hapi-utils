import org.hl7.fhir.r4.model.IntegerType
import org.hl7.fhir.r4.model.StringType


fun MutableList<StringType>.add(value: String) {
    this.add(StringType(value))
}

fun MutableList<IntegerType>.add(value: Int) {
    this.add(IntegerType(value))
}
