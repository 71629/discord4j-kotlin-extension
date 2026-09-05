package com.hashtag071629.event.reaction

import com.hashtag071629.event.EventListener
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.message.ReactionAddEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public object ReactionAddListener : EventListener<ReactionAddEvent, EventListener.Definition<ReactionAddEvent>>() {
    override val definition: Definition<ReactionAddEvent> get() = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    public fun GatewayDiscordClient.reactionAddListener(config: ReactionAddListener.() -> Unit) {
        config()
        on(ReactionAddEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    override suspend fun handle(event: ReactionAddEvent) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun Definition<ReactionAddEvent>.condition(block: (ReactionAddEvent) -> Boolean) {
        predicate = block
    }

    public fun Definition<ReactionAddEvent>.onReactionAdd(block: suspend (ReactionAddEvent) -> Unit) {
        action = block
    }
}

