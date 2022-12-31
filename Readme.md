# HAPI-Kotlin-Utils
Just a collection of functions I often use across my projects. They are intended to remove boilerplate code when 
populating FHIR resources with the HAPI FHIR library. 
Feel free to include or draw some inspiration!

## Features
* [Constructor-like functions for commonly used Data Types](#constructor-like-functions)
* [Utility functions for creating Coding instances of commonly used terminology systems](#CodeSystem-utility-functions) 
* [Create kotlin extension functions for FHIR Extensions](#Extension-properties-for-FHIR-Extensions)  
* [Conversions between HAPI's `DateType`, `DateTimeType` and `TimeType` and the corresponding `java.time` classes](#time-conversions)
* [`.toPrettyString()` extension function for logging/debugging purposes](#toprettystring) 
* [Extension functions for Questionnaires: `Questionnaire.allItems`, type aliases `QItem`, `QRItem`, `QRAnswer` for subitems](#extensions-on-questionnaire)
* [`.toCoding()` extension function for enums of the `org.hl7.fhir.r4.model.codesystems` package (ObservationCategory, ConditionCategory, ...)](#tocoding-extension-for-any-enum)
* [Add primitive values to list without wrapper class](#add-primitive-values-to-list)

## Why Kotlin?
Look at the following example from the [HAPI documentation](https://hapifhir.io/hapi-fhir/docs/model/working_with_resources.html):
```java
// Create an Observation instance
Observation observation = new Observation();

observation.setStatus(Observation.ObservationStatus.FINAL);

Coding coding = observation.getCode().addCoding();
coding.setCode("29463-7").setSystem("http://loinc.org").setDisplay("Body Weight");

Quantity value = new Quantity();
value.setValue(83.9).setSystem("http://unitsofmeasure.org").setCode("kg");
observation.setValue(value);

SimpleQuantity low = new SimpleQuantity();
low.setValue(45).setSystem("http://unitsofmeasure.org").setCode("kg");
observation.getReferenceRangeFirstRep().setLow(low);
SimpleQuantity high = new SimpleQuantity();
low.setValue(90).setSystem("http://unitsofmeasure.org").setCode("kg");
observation.getReferenceRangeFirstRep().setHigh(high);
```

This be easily rewritten with Kotlin and this package as:

```kotlin
val observation = Observation().apply {
    status = Observation.ObservationStatus.FINAL
    code = CodeableConcept(loinc("29463-7", "Body Weight"))
    value = UcumQuantity(83.9, "kg")
    referenceRangeFirstRep.low = UcumQuantity(45.0, "kg")
    referenceRangeFirstRep.high = UcumQuantity(90.0, "kg")
}
```
You can clearly see, that the code is much more concise and readable. This is especially important when writing ETL code
and large transformation pipelines.

## Constructor-like functions
For commonly used structures. E.g. instead of 
```kotlin
observation.identifier = listOf(Identifier().apply {
    system = "https://my-identifier-system/"
    value = "123456"
})
```
write 
```kotlin
observation.identifier = listOf(Identifier("https://my-identifier-system/", "123456"))
```
Supported functions:
* `Identifier(system, value, type, use, period, assigner)`
* `Period(start, end)`
* `Range(low, high)`
* `UcumQuantity(value, unit)`
* `CodeableConcept(codings...)`
* `Parameters(map)`

## CodeSystem utility functions
Normally, you would have to specify the CodeSystem's URI whenever you create a Coding instance:
```kotlin
val observation = Observation().apply {
 code = CodeableConcept().apply {
     coding = listOf(
        Coding("http://loinc.org", "29463-7", "Body Weight"),
        Coding("http://snomed.info/sct", "27113001", "Body weight (observable entity)")
     )
 }
}
```
For common code systems, this can be shortened:
```kotlin
val observation = Observation().apply {
 code = CodeableConcept(
     loinc("29463-7", "Body Weight"), 
     snomed("27113001", "Body weight (observable entity)")
 )   
}
```

## Extension properties for FHIR Extensions
In theory, you can [extend existing HAPI classes](https://hapifhir.io/hapi-fhir/docs/model/custom_structures.html) to 
integrate extensions into the class. To my experience, this is very cumbersome as you have to overwrite also 
`copy()` and `isEmpty()` and is very often not worth the effort. 
This library provides a much simpler solution to add properties to HAPI classes:
```kotlin
var QItem.hidden: Boolean? by extension("http://hl7.org/fhir/StructureDefinition/questionnaire-hidden")

fun doSomething(qItem: QItem) {
    if (qItem.hidden == null || qItem.hidden == false){
        render(qItem)
    }
}
```

## Time conversions
Use the modern `java.time` package with HAPI:
```kotlin
import org.hl7.fhir.r4.model.*
import java.time.*

val javaDateTime: LocalDateTime = LocalDateTime.of(2023, 1, 1, 12, 12, 12)
val fhirDateTime: DateTimeType = javaDateTime.toFhir()
val javaDateTime2: LocalDateTime = fhirDateTime.toLocalDateTime()
```

## toPrettyString()
HAPIs built-in classes usually don't have a good `toString()`-representation. In theory, you can create a JsonParser instance,
but you can stringify only IBaseResource, not individual elements. Therefore, this library provides the 
`.toPrettyString(multiline = true, printType = true)` function on any Element.

```kotlin
//does not help
println(patient.name[0].toString()) // 'org.hl7.fhir.r4.model.HumanName@725bef66' 

//does not compile
val parser = FhirContext.forR4().newJsonParser()
println(parser.encodeResourceToString(patient.name[0])) // IBaseResource is required

// :) 
println(patient.name[0].toPrettyString(multiline = false)) // 'HumanName{ family: "Doe",  given: ["John"] }'
```

## Extensions on Questionnaire
Adds some useful type aliases:
```kotlin
typealias QItem = Questionnaire.QuestionnaireItemComponent
typealias QRItem = QuestionnaireResponse.QuestionnaireResponseItemComponent
typealias QRAnswer = QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent
```

Adds utility function 'Questionnaire.allItems' and 'QuestionnaireResponse.allItems'
```kotlin
val item1: QItem? = questionnaire.allItems.find { it.linkId == "1.2.3" }
//can be simplified as 
val item1 = questionnaire["1.2.3"]
val item2: QRItem? = questionnaireResponse["1.2.3"]
```

## toCoding() extension for any enum
Convert the built-in enums for FHIR's built-in CodeSystems into Coding/CodeableConcept  

```kotlin
observation.category = listOf(ObservationCategory.ACTIVITY.toCodeableConcept())
```

Attention: This is based on reflection and can be called on any enum, not just HAPI enums and is therefore not typesafe.

## Add primitive values to List
normally you would have to wrap the value:
```kotlin
patient.nameFirstRep.given.add(StringType("John"))
```
or use the attribute-specific function of the containing datatype:
```kotlin
patient.nameFirstRep.addGiven("John")
```
you can write now:
```kotlin
patient.nameFirstRep.given.add("John")
```
## Others 
* Adds `in` operator to Period datatype: `condition.recordedDate in encounter.actualPeriod` (Caution: Does not account for DateTime precision)
* The `Parameters` class overrides the `get(...)` and `set(...)` operator, e.g.  `val persists: Boolean? = parameters["persist"]`