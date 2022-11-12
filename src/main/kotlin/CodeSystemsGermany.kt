import org.hl7.fhir.r4.model.Coding

/**
 * version is not-nullable on purpose
 */
fun ops(version: String, code: String, display: String? = null, seitenlokalisation: String? = null) =
    Coding("http://fhir.de/CodeSystem/bfarm/ops", code, display).apply {
        setVersion(version)
        if (seitenlokalisation != null) {
            addExtension(
                "http://fhir.de/StructureDefinition/seitenlokalisation",
                Coding("https://fhir.kbv.de/ValueSet/KBV_VS_SFHIR_ICD_SEITENLOKALISATION", seitenlokalisation, null)
            )
        }
    }

fun orphanet(code: String, display: String? = null) = Coding("http://www.orpha.net", code, display)

/**
 * ICD10 German Modification. Version requirement is on purpose
 */
fun icd10gm(
    version: String,
    code: String,
    display: String? = null,
    mehrfachcodierung: Icd10GmMehrfachkodierungskennzeichen? = null,
    seitenlokalisation: Icd10GmSeitenlokalisation? = null,
    diagnoseSicherheit: Icd10GmDiagnoseSicherheit? = null
) =
    Coding("http://fhir.de/CodeSystem/bfarm/icd-10-gm", code, display).apply {
        setVersion(version)
        if (mehrfachcodierung != null) {
            addExtension("http://fhir.de/StructureDefinition/icd-10-gm-mehrfachcodierungs-kennzeichen", mehrfachcodierung.toCoding())
        }
        if (seitenlokalisation != null) {
            addExtension("http://fhir.de/StructureDefinition/seitenlokalisation", seitenlokalisation.toCoding())
        }
        if (diagnoseSicherheit != null) {
            addExtension("http://fhir.de/StructureDefinition/icd-10-gm-diagnosesicherheit", diagnoseSicherheit.toCoding())
        }

    }

enum class Icd10GmDiagnoseSicherheit(val code: String, val display: String) {
    A("A", "ausgeschlossen"),
    G("G", "gesicherte Diagnose"),
    V("V", "Verdacht auf / zum Ausschluss von"),
    Z("Z", "Zustand nach");

    fun toCoding() = Coding("https://fhir.kbv.de/CodeSytem/KBV_VS_SFHIR_ICD_DIAGNOSESICHERHEIT", this.code, this.display)
}

enum class Icd10GmSeitenlokalisation(val code: String, val display: String) {
    L("L", "links"),
    R("R", "rechts"),
    B("B", "beiderseits");

    fun toCoding() = Coding("https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_SEITENLOKALISATION", this.code, this.display)

    companion object{
        fun parse(valueToParse: String): Icd10GmSeitenlokalisation? {
            return Icd10GmSeitenlokalisation.values().find { valueToParse.uppercase() == it.code || valueToParse == it.display }
        }
    }
}

enum class Icd10GmMehrfachkodierungskennzeichen(val code: String, val display: String) {
    STAR("*", "*"),
    CROSS("†", "†"),
    EXCLAMATION("!", "!");

    fun toCoding() = Coding("http://fhir.de/CodeSystem/icd-10-gm-mehrfachcodierungs-kennzeichen", this.code, this.display)

    companion object{
        fun parse(valueToParse: String)= Icd10GmMehrfachkodierungskennzeichen.values().find { valueToParse == it.code}
    }

}

fun alphaId(version: String, code: String, display: String? = null) =
    Coding("http://fhir.de/CodeSystem/bfarm/alpha-id", code, display).apply { setVersion(version) }

fun alphaIdSE(version: String, code: String, display: String? = null) =
    Coding("http://fhir.de/CodeSystem/bfarm/alpha-id-se", code, display).apply { setVersion(version) }

/**
 * deutsche fassung
 */
fun atc_de(code: String, display: String? = null) = Coding("http://fhir.de/CodeSystem/dimdi/atc", code, display)

fun pzn(code: String, display: String? = null) = Coding("http://fhir.de/CodeSystem/ifa/pzn", code, display)
