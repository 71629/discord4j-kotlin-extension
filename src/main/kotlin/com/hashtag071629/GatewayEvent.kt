package com.hashtag071629

import discord4j.core.event.domain.Event

public abstract class GatewayEvent<T : Event> {
    public abstract suspend fun handle(event: T)

    public open suspend fun onException(event: T, e: Throwable): Unit = e.printStackTrace()
}