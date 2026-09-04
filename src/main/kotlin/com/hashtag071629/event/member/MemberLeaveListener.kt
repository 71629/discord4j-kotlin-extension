package com.hashtag071629.event.member

import com.hashtag071629.event.EventListener
import discord4j.core.event.domain.guild.MemberLeaveEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public object MemberLeaveListener : EventListener<MemberLeaveEvent, EventListener.Definition<MemberLeaveEvent>>() {
    override val definition: Definition<MemberLeaveEvent> = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    override suspend fun handle(event: MemberLeaveEvent) {
        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun Definition<MemberLeaveEvent>.condition(block: (MemberLeaveEvent) -> Boolean) {
        predicate = block
    }

    public fun Definition<MemberLeaveEvent>.onLeave(block: suspend (MemberLeaveEvent) -> Unit) {
        action = block
    }
}