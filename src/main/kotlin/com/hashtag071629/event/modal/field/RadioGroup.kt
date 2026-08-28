package com.hashtag071629.event.modal.field

import com.hashtag071629.component.MultioptionChoice
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.RadioGroupAction
import kotlin.jvm.optionals.getOrNull

public class RadioGroup internal constructor(
    customId: String,
    fieldName: String = customId,
    description: String? = null,
    public var options: List<MultioptionChoice> = emptyList(),
) : DefaultValueField<String, RadioGroupAction>(customId, fieldName, description, options.firstOrNull { it.isDefault }?.value), MultioptionField {

    init {
        require(options.size in 2..10) { "Would be a BAD_REQUEST: The number of radio group options must sit between 2 and 10." }
        require(options.count { it.isDefault } <= 1) { "Would be a BAD_REQUEST: Radio group options must have no more than one default option." }
        require(options.map { it.value }.toSet().size == options.size) { "Would be a BAD_REQUEST: Radio group options must have unique values." }
        require(options.flatMap { listOf(it.value, it.label, it.description) }.none { (it?.length ?: 0) > 100 }) { "Would be a BAD_REQUEST: Radio group options must have no more than 100 characters in each field." }
    }

    override val component: RadioGroupAction
        get() = RadioGroupAction.of(customId, options.map {
        if (it.isDefault) RadioGroupAction.Option.ofDefault(it.label, it.value)
        else RadioGroupAction.Option.of(it.label, it.value)
    }).required(required)

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: RadioGroupAction) {
        userValue = component.value.getOrNull()
    }
}