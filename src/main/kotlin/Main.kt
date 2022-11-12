import org.hl7.fhir.r4.model.*
import org.hl7.fhir.r4.model.Identifier.IdentifierUse
import java.time.LocalDateTime

fun Identifier(
    system: String,
    value: String,
    type: CodeableConcept? = null,
    use: IdentifierUse? = null,
    period: Period? = null,
    assigner: Reference? = null,
) = Identifier().apply {
    this.system = system
    this.value = value
    this.use = use
    this.period = period
    this.type = type
    this.assigner = assigner
}

operator fun List<Identifier>.get(system: String): Identifier? {
    return this.find { it.system == system }
}

operator fun MutableList<Identifier>.set(system: String, value: String) {
    val current = this[system]
    if (current != null) {
        current.value = value
    } else {
        this.add(Identifier(system, value))
    }
}

/**
 * Fake Constructor
 */
fun Period(start: DateTimeType?, end: DateTimeType?) = Period().apply {
    startElement = start
    endElement = end
}

/**
 * Fake constructor
 */
fun Period(start: LocalDateTime?, end: LocalDateTime?) = Period(start?.toFhir(), end?.toFhir())

/**
 * Fake constructor
 */
fun Range(low: Quantity?, high: Quantity?) = Range().apply {
    this.low = low
    this.high = high
}

/**
 * Fake constructor
 */
fun UcumQuantity(value: Double, unit: String) = Quantity(null, value, "http://unitsofmeasure.org", unit, unit)


/**
 * Fake constructor for logical references
 */
fun Reference(identifier: Identifier): Reference = Reference().setIdentifier(identifier)

val DomainResource.patientDefiningElement: Reference
    get() = when (this) {
        is Observation -> this.subject
        is DiagnosticReport -> this.subject
        is Condition -> this.subject
        is Immunization -> this.patient
        is ImmunizationEvaluation -> this.patient
        is ImmunizationRecommendation -> this.patient
        is Consent -> this.patient
        is Procedure -> this.subject
        is QuestionnaireResponse -> this.subject
        is MedicationStatement -> this.subject
        is MedicationRequest -> this.subject
        is MedicationAdministration -> this.subject
        is MedicationDispense -> this.subject
        is Encounter -> this.subject
        is MolecularSequence -> this.patient
        is ImagingStudy -> this.subject
        is AllergyIntolerance -> this.patient
        is AdverseEvent -> this.subject
        is FamilyMemberHistory -> this.patient
        is ClinicalImpression -> this.subject
        is DetectedIssue -> this.patient
        is Media -> this.subject
        is Specimen -> this.subject
        is BodyStructure -> this.patient
        is CarePlan -> this.subject
        is CareTeam -> this.subject
        is Goal -> this.subject
        is ServiceRequest -> this.subject
        is NutritionOrder -> this.patient
        is VisionPrescription -> this.patient
        is RiskAssessment -> this.subject
        is RequestGroup -> this.subject
        is Communication -> this.subject
        is CommunicationRequest -> this.subject
        is DeviceRequest -> this.subject
        is DeviceUseStatement -> this.subject
        is GuidanceResponse -> this.subject
        is ResearchSubject -> this.individual
        else -> error("Unknown resource type '${this.resourceType}'. Cannot retrieve subject/patient reference!")
    }

fun Base.toPrettyString(indentation: String = "", multiline: Boolean = true, printType: Boolean = true): String {
    if(this is PrimitiveType<*> && !this.hasExtension()) {
        return stringifyPrimitiveType()!!
    }

    val indent = if(multiline) indentation else ""
    val listSeperator = if(multiline) ",\n" else ", "
    return buildString {
        if (!isPrimitive && printType) {
            append(fhirType())
        }
        if(isPrimitive) {
            append(fhirType())
            append("(")
            append(stringifyPrimitiveType())
            append(")")
        }
        if (multiline && printType) {
            append(" ")
        }
        if (multiline) {
            append("{\n")
        } else {
            append("{")
        }
        append(children().filter { it.hasValues() }
//            .filter { it.isList.not() && it.values.first().isEmpty }
            .joinToString(listSeperator) {
            indent+" "+it.name+": "+
                    if(it.isList) it.values.joinToString(", ", "[", "]") { it.toPrettyString("$indent ", multiline, printType) } else it.values.first().toPrettyString(
                        "$indent ", multiline, printType
                    ) })
        if(multiline){
            append("\n")
        } else {
            append(" ")
        }
        append("$indent}")
    }
}

private fun Base.stringifyPrimitiveType(): String? {
    if (this is StringType) {
        return if (hasValue()) "\"" + primitiveValue() + "\"" else "null" //make "null" and null distiguishable
    }
    return if (hasPrimitiveValue()) primitiveValue() else "null"
}


operator fun Parameters.get(name: String): Type? = this.getParameter(name)
operator fun Parameters.set(name: String, type: String) {
    this.setParameter(name, type)
}
operator fun Parameters.set(name: String, type: Type) {
    this.setParameter(name, type)
}
operator fun Parameters.set(name: String, type: Boolean) {
    this.setParameter(name, type)
}
