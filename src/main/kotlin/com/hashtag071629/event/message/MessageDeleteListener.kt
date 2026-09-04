package com.hashtag071629.event.message

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.MessageDeleteEvent
import kotlinx.coroutines.reactor.mono

public object MessageDeleteListener : MessageListener<MessageDeleteEvent>() {

    public fun GatewayDiscordClient.messageDeleteListener(config: MessageDeleteListener.() -> Unit) {
        config()
        on(MessageDeleteEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    public fun Definition<MessageDeleteEvent>.onDelete(block: suspend (MessageDeleteEvent) -> Unit) {
        action = block
    }
}