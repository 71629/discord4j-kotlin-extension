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

    init {
        require(minLength in 0..4000) { "Would be a BAD_REQUEST: minLength must sit between 0 and 4000" }
        require(maxLength in 0..4000) { "Would be a BAD_REQUEST: maxLength must sit between 0 and 4000" }
        require(minLength <= maxLength) { "Would be a BAD_REQUEST: minLength must be less than or equal to maxLength" }
        defaultValue?.let {
            require(it.length in minLength..maxLength) { "Would be a BAD_REQUEST: defaultValue must sit between minLength and maxLength" }
        }
    }

    override val component: TextInput
        get() = TextInput.of(style, customId, minLength, maxLength, defaultValue, placeholder)

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: TextInput) {
        userValue = component.value.getOrNull()
    }
}