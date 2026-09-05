package com.hashtag071629.event.member

import com.hashtag071629.event.EventListener
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.guild.MemberJoinEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public object MemberJoinListener : EventListener<MemberJoinEvent, EventListener.Definition<MemberJoinEvent>>() {
    override val definition: Definition<MemberJoinEvent> = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    public fun GatewayDiscordClient.memberJoinListener(config: MemberJoinListener.() -> Unit) {
        config()
        on(MemberJoinEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    override suspend fun handle(event: MemberJoinEvent) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun Definition<MemberJoinEvent>.condition(block: (MemberJoinEvent) -> Boolean) {
        predicate = block
    }

    public fun Definition<MemberJoinEvent>.onJoin(block: suspend (MemberJoinEvent) -> Unit) {
        action = block
    }
}