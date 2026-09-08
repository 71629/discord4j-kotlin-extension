package com.hashtag071629.event.modal.field

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.SelectMenu
import kotlin.jvm.optionals.getOrNull

public class StringSelect internal constructor(
    customId: String,
    fieldName: String = customId,
    description: String? = null,
    placeholder: String? = null,
    minValues: Int = 1,
    maxValues: Int = 1,
    public var options: List<SelectMenu.Option> = emptyList()
) : SelectField<String>(customId, emptyList(), fieldName, description, placeholder, minValues, maxValues) {
    override val component = SelectMenu.of(customId, options).setCommonData().let {
        placeholder?.let { p -> it.withPlaceholder(p) } ?: it
    }

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: SelectMenu) {
        userValue = component.values.getOrNull() ?: emptyList()
    }
}