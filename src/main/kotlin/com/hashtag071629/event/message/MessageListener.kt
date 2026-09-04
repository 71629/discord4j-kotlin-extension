package com.hashtag071629.event.message

import discord4j.core.event.domain.message.MessageEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public abstract class MessageListener<T : MessageEvent> internal constructor() : EventListener<T, EventListener.Definition<T>>() {
    override val definition: Definition<T> get() = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    override suspend fun handle(event: T) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun Definition<T>.condition(block: (T) -> Boolean) {
        predicate = block
    }
}