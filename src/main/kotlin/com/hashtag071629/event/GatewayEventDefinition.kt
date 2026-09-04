package com.hashtag071629.event

import discord4j.core.event.domain.Event

public abstract class GatewayEventDefinition<T : Event> {
    internal abstract suspend fun handle(event: T)

    public open suspend fun onException(event: T, e: Throwable): Unit = e.printStackTrace()
}