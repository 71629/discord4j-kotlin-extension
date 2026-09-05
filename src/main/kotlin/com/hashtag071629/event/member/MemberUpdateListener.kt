package com.hashtag071629.event.member

import com.hashtag071629.event.EventListener
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.guild.MemberUpdateEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public object MemberUpdateListener : EventListener<MemberUpdateEvent, EventListener.Definition<MemberUpdateEvent>>() {
    override val definition: Definition<MemberUpdateEvent> = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    public fun GatewayDiscordClient.memberUpdateListener(config: MemberUpdateListener.() -> Unit) {
        config()
        on(MemberUpdateEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    override suspend fun handle(event: MemberUpdateEvent) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun Definition<MemberUpdateEvent>.condition(block: (MemberUpdateEvent) -> Boolean) {
        predicate = block
    }

    public fun Definition<MemberUpdateEvent>.onUpdate(block: suspend (MemberUpdateEvent) -> Unit) {
        action = block
    }
}