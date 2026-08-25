package com.hashtag071629

import discord4j.core.event.domain.Event

public abstract class GatewayEvent<T : Event> {
    public abstract suspend fun T.handle()

    public open suspend fun T.onException(e: Throwable): Unit = e.printStackTrace()
}