package com.hashtag071629.event.modal.field

import discord4j.core.`object`.component.ICanBeUsedInLabelComponent
import discord4j.core.`object`.component.MessageComponent

public sealed class FreeInputModalField<R, C>(
    customId: String,
    defaultValue: R?,
    fieldName: String,
    description: String?,
    public var placeholder: String?,
) : DefaultValueField<R, C>(customId, fieldName, description, defaultValue) where C : MessageComponent, C : ICanBeUsedInLabelComponent