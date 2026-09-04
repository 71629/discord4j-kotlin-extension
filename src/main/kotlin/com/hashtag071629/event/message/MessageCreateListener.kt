package com.hashtag071629.event.message

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.reactor.mono

public object MessageCreateListener : MessageListener<MessageCreateEvent>() {

    public fun GatewayDiscordClient.messageCreateListener(config: MessageCreateListener.() -> Unit) {
        config()
        on(MessageCreateEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    public fun Definition<MessageCreateEvent>.onReceive(block: suspend (MessageCreateEvent) -> Unit) {
        action = block
    }
}
