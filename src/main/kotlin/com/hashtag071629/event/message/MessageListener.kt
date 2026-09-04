package com.hashtag071629.event.message

import com.hashtag071629.annotations.ClientMarker
import com.hashtag071629.event.GatewayEvent
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.message.MessageEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

@ClientMarker
public abstract class MessageListener<T : MessageEvent> internal constructor() {
    private val listeners = mutableListOf<Definition<T>>()
    public var maxConcurrency: Int = Int.MAX_VALUE

    private val definition get() = Definition<T>()

    public fun install(config: Definition<T>.() -> Unit) {
        val listener = definition.apply(config)
        listeners.add(listener)
    }

    internal fun handle(event: T) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public class Definition<T : Event> internal constructor() : GatewayEvent<T>() {
        internal var predicate: (T) -> Boolean = { true }
        internal var action: suspend (T) -> Unit = {}
        internal var onException: (suspend (T, Throwable) -> Unit)? = null

        public fun condition(block: (T) -> Boolean) {
            predicate = block
        }

        override suspend fun handle(event: T) {
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
}