package com.hashtag071629.event.modal.field

import com.hashtag071629.component.MultioptionChoice
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.CheckboxGroupAction
import kotlin.jvm.optionals.getOrNull

public class CheckboxGroup internal constructor(
    customId: String,
    fieldName: String = customId,
    description: String? = null,
    public var options: List<MultioptionChoice> = emptyList()
) : DefaultValueField<List<String>, CheckboxGroupAction>(customId, fieldName, description, options.filter { it.isDefault }.map { it.value }), MultioptionField {
    init {
        require(options.size in 1..10) { "Would be a BAD_REQUEST: The number of checkbox group options must sit between 1 and 10." }
        require(options.map { it.value }.toSet().size == options.size) { "Would be a BAD_REQUEST: Checkbox group options must have unique values." }
        require(options.flatMap { listOf(it.value, it.label, it.description) }.none { (it?.length ?: 0) > 100 }) { "Would be a BAD_REQUEST: Checkbox group options must have no more than 100 characters in each field." }
    }

    override val component: CheckboxGroupAction
        get() = CheckboxGroupAction.of(customId, options.map {
        if (it.isDefault) CheckboxGroupAction.Option.ofDefault(it.label, it.value)
        else CheckboxGroupAction.Option.of(it.label, it.value)
    }).required(required)

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: CheckboxGroupAction) {
        userValue = component.values.getOrNull()?.toList() ?: emptyList()
    }
}