package com.hashtag071629.event.modal

import com.hashtag071629.client
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.ActionComponent
import discord4j.core.`object`.component.MessageComponent
import kotlinx.coroutines.reactor.mono
import reactor.util.Loggers

internal object CentralModalEventHandler {
    internal val listeners = mutableMapOf<String, Modal>()

    init {
        client.on(ModalSubmitInteractionEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    private suspend fun handle(event: ModalSubmitInteractionEvent) {
        listeners.remove(event.customId)?.handle(event)
    }
}