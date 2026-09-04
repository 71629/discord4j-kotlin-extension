package com.hashtag071629.event

import com.hashtag071629.annotations.ClientMarker
import discord4j.core.event.domain.Event

@ClientMarker
public abstract class EventListener<E : Event, D : EventListener.Definition<E>> internal constructor() {
    protected val listeners: MutableList<D> = mutableListOf()
    protected abstract val definition: D

    public fun install(config: D.() -> Unit) {
        val def = definition.apply(config)
        listeners.add(def)
    }

    protected abstract suspend fun handle(event: E)

    protected open suspend fun D.handle(event: E) {
        runCatching {
            action.invoke(event)
        }.onFailure {
            it.printStackTrace()
            onException?.invoke(event, it)
        }
    }

    public fun D.onException(block: suspend (E, Throwable) -> Unit) {
        onException = block
    }

    public open class Definition<T : Event> internal constructor() {
        internal open var predicate: (T) -> Boolean = { true }
        internal var action: suspend (T) -> Unit = {}
        internal var onException: (suspend (T, Throwable) -> Unit)? = null
    }
}