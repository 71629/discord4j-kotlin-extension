package com.hashtag071629.event.modal.field

import discord4j.core.`object`.component.SelectMenu

public sealed class SelectField<R>(
    customId: String,
    defaultValues: List<R> = emptyList(),
    fieldName: String = customId,
    description: String? = null,
    placeholder: String? = null,
    public var minValues: Int,
    public var maxValues: Int,
) : FreeInputModalField<List<R>, SelectMenu>(customId, defaultValues, fieldName, description, placeholder) {
    protected val setCommonData: SelectMenu.() -> SelectMenu = {
        withMinValues(this@SelectField.minValues).withMaxValues(this@SelectField.maxValues).required(required)
    }
}