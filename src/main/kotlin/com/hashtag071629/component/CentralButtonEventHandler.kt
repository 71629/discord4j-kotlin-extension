package com.hashtag071629.component

import com.hashtag071629.client
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ButtonInteractionEvent
import kotlinx.coroutines.reactor.mono

public object CentralButtonEventHandler {
    internal val listeners = mutableMapOf<String, ButtonActionDefinition>()

    init {
        client.on(ButtonInteractionEvent::class.java) { mono { handle(it) } }
    }

    public fun GatewayDiscordClient.button(config: ButtonConfigurator.() -> Unit) {
        ButtonConfigurator().apply(config)

        on(ButtonInteractionEvent::class.java) { mono { handle(it) } }.subscribe {  }
    }

    private suspend fun handle(event: ButtonInteractionEvent) {
        listeners[event.customId]?.let { handler ->
            runCatching {
                handler.onClick(event)
            }.onFailure {
                handler.onException(event, it)
            }
        }
    }
}