import org.hl7.fhir.r4.model.*
import org.hl7.fhir.r4.model.Enumerations.DataAbsentReason
import kotlin.properties.ReadWriteProperty

inline fun <reified T : Element> dataAbsentReason(type: DataAbsentReason): T {
    return dataAbsentReason(type.toCode())
}

inline fun <reified T : Element> dataAbsentReason(type: String): T {
    val extension = Extension("http://hl7.org/fhir/StructureDefinition/data-absent-reason", CodeType(type))
    if (T::class == Extension::class) { //Do not create an empty extension with an extension
        return extension as T
    }
    val element = T::class.java.getConstructor().newInstance()
    element.addExtension(extension)
    return element
}

/**
 * Reflection-less variant (slightly faster)
 */
fun <T : Element> T.dataAbsentReason(type: String): T {
    this.addExtension(Extension("http://hl7.org/fhir/StructureDefinition/data-absent-reason", CodeType(type)))
    return this
}

fun <T : Element> T.dataAbsentReason(type: DataAbsentReason): T {
    return dataAbsentReason(type.toCode())
}

var <T : Element> T.dataAbsentReason: DataAbsentReason?
    get() = getExtensionString("http://hl7.org/fhir/StructureDefinition/data-absent-reason")?.let { DataAbsentReason.fromCode(it) }
    set(value) {
        if (value != null) {
            this.dataAbsentReason(value)
        } else {
            this.removeExtension("http://hl7.org/fhir/StructureDefinition/data-absent-reason")
        }
    }


//TODO: Add such an extension for slicing, e.g. val icd10 by valueSlice(discriminatorPath)

inline fun <reified T: Any?>extension(extensionUrl: String): ReadWriteProperty<Element, T?> = object: ReadWriteProperty<Element, T?> {
    override operator fun getValue(thisRef: Element, property: kotlin.reflect.KProperty<*>): T? {
        val extension = thisRef.getExtensionByUrl(extensionUrl) ?: return null
        val value = extension.value
        if (T::class == String::class) {
            return (value as StringType).value as T
        }
        if (T::class == Int::class) {
            return (value as IntegerType).value as T
        }
        if (T::class == Boolean::class) {
            return (value as BooleanType).value as T
        }
        if (value is T) {
            return value
        } else {
            error("Wrong type for extension '$extensionUrl'. Cannot cast '$value' (${value::class}) to ${T::class}!")
        }
    }

    override operator fun setValue(thisRef: Element, property: kotlin.reflect.KProperty<*>, value: T?) {
        if (value == null) {
            thisRef.removeExtension(extensionUrl)
        } else {
            if (value is Type) {
                thisRef.addExtension(extensionUrl, value)
            }
            if (value is String) {
                thisRef.addExtension(extensionUrl, StringType(value))
            }
            if (value is Int) {
                thisRef.addExtension(extensionUrl, IntegerType(value))
            }
            if (value is Boolean) {
                thisRef.addExtension(extensionUrl, BooleanType(value))
            }
        }
    }
}

inline operator fun <reified T : Type?> List<Extension>.get(url: String): T {
    val value = this.find { it.url == url }?.value ?: return null as T
    return if (value is T) value else error("Expected value of extension '$url' to be of type ${T::class.simpleName}, but got ${value::class.simpleName} instead!")
}

operator fun MutableList<Extension>.set(url: String, value: Type) {
    this.removeIf { it.url == url }
    this.add(Extension(url, value))
}