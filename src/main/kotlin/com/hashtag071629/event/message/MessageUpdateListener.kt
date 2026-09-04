package com.hashtag071629.event.message

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.MessageUpdateEvent
import kotlinx.coroutines.reactor.mono

public object MessageUpdateListener : MessageListener<MessageUpdateListenerDefinition, MessageUpdateEvent>() {
    override val definition: MessageUpdateListenerDefinition get() = MessageUpdateListenerDefinition()

    public fun GatewayDiscordClient.messageUpdateListener(config: MessageUpdateListener.() -> Unit) {
        config()
        on(MessageUpdateEvent::class.java) { mono { handle(it) } }.subscribe()
    }
}