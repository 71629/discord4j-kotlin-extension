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

    override val component: SelectMenu
        get() = getSelectMenu()
        .withMinValues(minValues)
        .withMaxValues(maxValues)

    init {
        require(minValues in 0..25) { "Would be a BAD_REQUEST: minValues must sit between 0 and 25." }
        require(maxValues in 0..25) { "Would be a BAD_REQUEST: maxValues must sit between 0 and 25." }
        require(minValues <= maxValues) { "Would be a BAD_REQUEST: minValues must be less than or equal to maxValues." }
        require(defaultValues.size in minValues..maxValues) { "Would be a BAD_REQUEST: The number of default values must sit between minValues and maxValues." }
    }

    protected abstract fun getSelectMenu(): SelectMenu
}