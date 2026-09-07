package com.hashtag071629.event

import com.hashtag071629.annotations.ClientMarker
import discord4j.core.event.domain.Event

@ClientMarker
public abstract class EventListener<E : Event, D : EventListener.Definition<E>> internal constructor() {
    protected val listeners: MutableList<D> = mutableListOf()
    protected abstract val definition: D

    internal var beforeExecution: (D.(E) -> Unit)? = null
    internal var afterExecution: (D.(E) -> Unit)? = null
    internal var onException: (suspend D.(E, Throwable) -> Unit)? = null

    public fun install(config: D.() -> Unit) {
        val def = definition.apply(config)
        listeners.add(def)
    }

    public fun beforeExecution(block: D.(E) -> Unit) {
        beforeExecution = block
    }

    public fun afterExecution(block: D.(E) -> Unit) {
        afterExecution = block
    }

    public fun onException(block: suspend D.(E, Throwable) -> Unit) {
        onException = block
    }

    protected abstract suspend fun handle(event: E)

    protected suspend fun D.handle(event: E) {
        runCatching {
            if (!excludeFromBeforeExecution) beforeExecution?.invoke(this, event)
            action.invoke(event)
            if (!excludeFromAfterExecution) afterExecution?.invoke(this, event)
        }.onFailure {
            it.printStackTrace()
            if (!excludeGlobalOnException) this@EventListener.onException?.invoke(this, event, it)
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

        public var excludeFromBeforeExecution: Boolean = false
        public var excludeFromAfterExecution: Boolean = false
        public var excludeGlobalOnException: Boolean = false
    }
}