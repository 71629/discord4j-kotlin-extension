package com.hashtag071629.event.message

import com.hashtag071629.event.GatewayEvent
import discord4j.core.event.domain.message.MessageEvent

public abstract class MessageListenerDefinition<T : MessageEvent> internal constructor() : GatewayEvent<T>() {
    internal var predicate: (T) -> Boolean = { true }
    internal var action: suspend (T) -> Unit = {}
    internal var onException: (suspend (T, Throwable) -> Unit)? = null

    public fun condition(block: (T) -> Boolean) {
        predicate = block
    }

    final override suspend fun handle(event: T) {
        runCatching {
            action.invoke(event)
        }.onFailure {
            super.onException(event, it)
            onException?.invoke(event, it)
        }
    }

    public fun onException(block: suspend (T, Throwable) -> Unit) {
        onException = block
    }
}