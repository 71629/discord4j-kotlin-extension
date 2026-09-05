package com.hashtag071629.event.reaction

import com.hashtag071629.event.EventListener
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.ReactionAddEvent
import discord4j.core.event.domain.message.ReactionRemoveEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public object ReactionRemoveListener : EventListener<ReactionRemoveEvent, EventListener.Definition<ReactionRemoveEvent>>() {
    override val definition: Definition<ReactionRemoveEvent> get() = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    public fun GatewayDiscordClient.reactionRemoveListener(config: ReactionRemoveListener.() -> Unit) {
        config()
        on(ReactionRemoveEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    override suspend fun handle(event: ReactionRemoveEvent) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun Definition<ReactionRemoveEvent>.condition(block: (ReactionRemoveEvent) -> Boolean) {
        predicate = block
    }

    public fun Definition<ReactionRemoveEvent>.onReactionRemove(block: suspend (ReactionRemoveEvent) -> Unit) {
        action = block
    }
}