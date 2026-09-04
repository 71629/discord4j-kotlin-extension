package com.hashtag071629.event.message

import com.hashtag071629.annotations.ClientMarker
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.message.MessageEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

@ClientMarker
public abstract class EventListener<T : Event> internal constructor() {
    protected val listeners: MutableList<Definition<T>> = mutableListOf()
    protected val definition: Definition<T> get() = Definition()

    public fun install(config: Definition<T>.() -> Unit) {
        val def = definition.apply(config)
        listeners.add(def)
    }

    protected abstract suspend fun handle(event: T)

    protected suspend fun Definition<T>.handle(event: T) {
        runCatching {
            action.invoke(event)
        }.onFailure {
            it.printStackTrace()
            onException?.invoke(event, it)
        }
    }

    public fun Definition<T>.onException(block: suspend (T, Throwable) -> Unit) {
        onException = block
    }

    public open class Definition<T : Event> internal constructor() {
        internal open var predicate: (T) -> Boolean = { true }
        internal var action: suspend (T) -> Unit = {}
        internal var onException: (suspend (T, Throwable) -> Unit)? = null
    }
}

public abstract class MessageListener<T : MessageEvent> internal constructor() : EventListener<T>() {
    public var maxConcurrency: Int = Int.MAX_VALUE

    override suspend fun handle(event: T) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun Definition<T>.condition(block: (T) -> Boolean) {
        predicate = block
    }
}