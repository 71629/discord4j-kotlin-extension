package com.hashtag071629.component

import discord4j.core.event.domain.interaction.ButtonInteractionEvent

public class ButtonActionDefinition(internal val customId: String) {
    internal var onClick: suspend (ButtonInteractionEvent) -> Unit = { _ -> }
    internal var onException: suspend (ButtonInteractionEvent, Throwable) -> Unit = { _, _ -> }

    public fun onClick(definition: suspend (ButtonInteractionEvent) -> Unit) {
        onClick = definition
    }

    public fun onException(definition: suspend (ButtonInteractionEvent, Throwable) -> Unit) {
        onException = definition
    }
}