package com.hashtag071629.event.modal.field

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.TextInput
import kotlin.jvm.optionals.getOrNull

public class TextField internal constructor(
    customId: String,
    defaultValue: String? = null,
    fieldName: String = customId,
    description: String? = null,
    placeholder: String? = null,
    public var minLength: Int = 0,
    public var maxLength: Int = 4000,
    public var style: TextInput.Style = TextInput.Style.SHORT,
) : FreeInputModalField<String, TextInput>(customId, defaultValue, fieldName, description, placeholder) {

    override val component: TextInput
        get() = TextInput.of(style, customId, minLength, maxLength, defaultValue, placeholder).required(required)

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: TextInput) {
        userValue = component.value.getOrNull()
    }
}