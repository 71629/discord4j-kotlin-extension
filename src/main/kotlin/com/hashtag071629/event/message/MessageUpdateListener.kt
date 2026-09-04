package com.hashtag071629.event.message

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.MessageUpdateEvent
import kotlinx.coroutines.reactor.mono

public object MessageUpdateListener : MessageListener<MessageUpdateEvent>() {
    public fun GatewayDiscordClient.messageUpdateListener(config: MessageUpdateListener.() -> Unit) {
        config()
        on(MessageUpdateEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    public fun Definition<MessageUpdateEvent>.onEdit(block: suspend (MessageUpdateEvent) -> Unit) {
        action = block
    }
}