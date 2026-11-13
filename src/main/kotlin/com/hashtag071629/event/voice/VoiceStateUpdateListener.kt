package com.hashtag071629.event.voice

import com.hashtag071629.event.EventListener
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.VoiceStateUpdateEvent
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux

public object VoiceStateUpdateListener : EventListener<VoiceStateUpdateEvent, EventListener.Definition<VoiceStateUpdateEvent>>() {
    override val definition: Definition<VoiceStateUpdateEvent> get() = Definition()
    public var maxConcurrency: Int = Int.MAX_VALUE

    override suspend fun handle(event: VoiceStateUpdateEvent) {
        Flux.fromIterable(listeners)
            .filter { it.predicate(event) }
            .flatMap({ mono { it.handle(event) } }, maxConcurrency)
            .subscribe()

        val matches = listeners.filter { it.predicate(event) }.map { suspend { it.handle(event) } }
        Flux.fromIterable(matches).flatMap({ mono { it() } }, maxConcurrency).subscribe()
    }

    public fun GatewayDiscordClient.voiceStateUpdateListener(config: VoiceStateUpdateListener.() -> Unit) {
        config()
        on(VoiceStateUpdateEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    public fun Definition<VoiceStateUpdateEvent>.condition(block: (VoiceStateUpdateEvent) -> Boolean) {
        predicate = block
    }
}