package com.hashtag071629.event.reaction

import com.hashtag071629.event.EventListener
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.ReactionRemoveAllEvent
import discord4j.core.event.domain.message.ReactionRemoveEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public object ReactionRemoveAllListener : EventListener<ReactionRemoveAllEvent, EventListener.Definition<ReactionRemoveAllEvent>>() {
    override val definition: Definition<ReactionRemoveAllEvent> get() = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    public fun GatewayDiscordClient.reactionRemoveAllListener(config: ReactionRemoveAllListener.() -> Unit) {
        config()
        on(ReactionRemoveAllEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    override suspend fun handle(event: ReactionRemoveAllEvent) {
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