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