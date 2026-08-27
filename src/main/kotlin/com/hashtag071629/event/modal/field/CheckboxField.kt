package com.hashtag071629.event.modal.field

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.CheckboxAction

public class CheckboxField internal constructor(
    customId: String,
    fieldName: String = customId,
    description: String? = null,
    defaultValue: Boolean = false,
) : DefaultValueField<Boolean, CheckboxAction>(customId, fieldName, description, defaultValue) {
    override val component: CheckboxAction get() = CheckboxAction.of(customId)

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: CheckboxAction) {
        userValue = component.value.get()
    }
}