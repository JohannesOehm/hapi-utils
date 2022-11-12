import jdk.jshell.Diag
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Extension



fun snomed(code: String, display: String? = null) = Coding("http://snomed.info/sct", code, display)

fun loinc(code: String, display: String? = null) = Coding("http://loinc.org", code, display)

fun rxNorm(code: String, display: String? = null) = Coding("http://www.nlm.nih.gov/research/umls/rxnorm", code, display)

fun ucum(code: String) = Coding("http://unitsofmeasure.org", code, code)
