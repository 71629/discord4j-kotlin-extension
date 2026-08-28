package com.hashtag071629.component

import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent
import discord4j.core.`object`.component.SelectMenu

public abstract class SelectMenuBuilder internal constructor(protected val customId: String) : ComponentDsl() {
    public var allowedValues: IntRange = 1..1
    public var placeholder: String? = null
    public var disabled: Boolean = false

    protected var onSubmit: suspend (SelectMenuInteractionEvent) -> Unit = {
        it.deferReply()
        throw NotImplementedError()
    }

    public fun onSubmit(action: suspend (SelectMenuInteractionEvent) -> Unit) {
        onSubmit = action
    }

    protected fun buildAll(selectMenu: SelectMenu): SelectMenu {
        return placeholder?.let { selectMenu.withPlaceholder(it) } ?: selectMenu
            .withMinValues(allowedValues.first)
            .withMaxValues(allowedValues.last)
            .disabled(disabled)
    }
}