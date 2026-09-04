package com.hashtag071629.event.message

import com.hashtag071629.annotations.ClientMarker
import discord4j.core.event.domain.message.MessageEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

@ClientMarker
public abstract class MessageListener<L : MessageListenerDefinition<T>, T : MessageEvent> internal constructor() {
    internal val listeners = mutableListOf<L>()
    public var maxConcurrency: Int = Int.MAX_VALUE

    internal abstract val definition: L

    public fun install(config: L.() -> Unit) {
        val listener = definition.apply(config)
        listeners.add(listener)
    }

    internal fun handle(event: T) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }
}