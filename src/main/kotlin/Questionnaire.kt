import org.hl7.fhir.r4.model.Questionnaire
import org.hl7.fhir.r4.model.QuestionnaireResponse
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent

typealias QRItem = QuestionnaireResponse.QuestionnaireResponseItemComponent
typealias QItem = Questionnaire.QuestionnaireItemComponent
typealias QRAnswer = QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent

val Questionnaire.allItems: List<QItem>
    get() = allItemsSeq.toList()

val Questionnaire.allItemsSeq: Sequence<QItem>
    get() = this.item.asSequence() + this.item.asSequence().flatMap { it.allItemsSeq }

val QItem.allItems: List<QItem>
    get() = this.item + this.item.flatMap { it.allItemsSeq }

val QItem.allItemsSeq: Sequence<QItem>
    get() = this.item.asSequence() + this.item.asSequence().flatMap { it.allItemsSeq }

val QuestionnaireResponse.allItems: List<QRItem>
    get() = this.item + this.item.flatMap { it.allItems }

val QRItem.allItems: List<QRItem>
    get() = this.item + this.item.flatMap { it.allItems } + this.answer.flatMap { it.allItems }

val QRAnswer.allItems: List<QRItem>
    get() = this.item + this.item.flatMap { it.allItems }

//TODO: ist das mit der Sequence schneller?
operator fun Questionnaire.get(linkId: String): QItem? {
    return allItemsSeq.find { it.linkId == linkId }
}

fun QItem.get(linkId: String): QItem? {
    return allItemsSeq.find { it.linkId == linkId }
}
