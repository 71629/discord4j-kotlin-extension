package com.hashtag071629.event.modal.field

import discord4j.core.`object`.component.ICanBeUsedInLabelComponent
import discord4j.core.`object`.component.MessageComponent

public sealed class DefaultValueField<R, C>(
    customId: String,
    fieldName: String,
    description: String?,
    public val defaultValue: R?,
) : ModalField<R, C>(customId, fieldName, description) where C : MessageComponent, C : ICanBeUsedInLabelComponent