package com.hashtag071629.event.modal.field

import discord4j.core.`object`.component.SelectMenu

public sealed class

SelectField<R>(
    customId: String,
    defaultValues: List<R> = emptyList(),
    fieldName: String = customId,
    description: String? = null,
    placeholder: String? = null,
    public var minValues: Int,
    public var maxValues: Int,
) : FreeInputModalField<List<R>, SelectMenu>(customId, defaultValues, fieldName, description, placeholder) {

    override val component: SelectMenu get() = getSelectMenu()
        .withMinValues(minValues)
        .withMaxValues(maxValues)
        .required(required)

    protected abstract fun getSelectMenu(): SelectMenu
}