package com.hashtag071629.event.message

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.reactor.mono

public object MessageCreateListener : MessageListener<MessageCreateListenerDefinition, MessageCreateEvent>() {
    override val definition: MessageCreateListenerDefinition get() = MessageCreateListenerDefinition()

    public fun GatewayDiscordClient.messageCreateListener(config: MessageCreateListener.() -> Unit) {
        config()
        on(MessageCreateEvent::class.java) { mono { handle(it) } }.subscribe()
    }
}
