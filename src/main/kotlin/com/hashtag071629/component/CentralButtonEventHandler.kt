package com.hashtag071629.component

import com.hashtag071629.event.EventListener
import com.hashtag071629.event.slash.SlashCommand
import com.hashtag071629.event.slash.SlashCommand.handle
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ButtonInteractionEvent
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import kotlinx.coroutines.reactor.mono

public object CentralButtonEventHandler : EventListener<ButtonInteractionEvent, CentralButtonEventHandler.Definition>() {
    override val definition: Definition get() = Definition()

    public fun GatewayDiscordClient.button(config: CentralButtonEventHandler.() -> Unit) {
        apply(config)
        on(ButtonInteractionEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    public fun Definition.onClick(block: suspend (ButtonInteractionEvent) -> Unit) {
        action = block
    }

    override suspend fun handle(event: ButtonInteractionEvent) {
        listeners.firstOrNull { it.predicate(event) }?.handle(event)
    }

    public class Definition internal constructor() : EventListener.Definition<ButtonInteractionEvent>() {
        internal lateinit var customId: String
        override var predicate: (ButtonInteractionEvent) -> Boolean = { it.customId == customId }
    }
}