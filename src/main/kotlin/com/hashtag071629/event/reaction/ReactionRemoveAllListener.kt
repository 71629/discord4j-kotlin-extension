package com.hashtag071629.event.reaction

import com.hashtag071629.event.message.EventListener
import discord4j.core.event.domain.message.ReactionRemoveAllEvent
import discord4j.core.event.domain.message.ReactionRemoveEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public object ReactionRemoveAllListener : EventListener<ReactionRemoveEvent, EventListener.Definition<ReactionRemoveEvent>>() {
    override val definition: Definition<ReactionRemoveEvent> get() = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    override suspend fun handle(event: ReactionRemoveEvent) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun Definition<ReactionRemoveAllEvent>.condition(block: (ReactionRemoveAllEvent) -> Boolean) {
        predicate = block
    }

    public fun Definition<ReactionRemoveEvent>.onReactionRemoveAll(block: suspend (ReactionRemoveEvent) -> Unit) {
        action = block
    }
}