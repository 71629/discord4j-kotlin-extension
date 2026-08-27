package com.hashtag071629.event.modal

import com.hashtag071629.client
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.ActionComponent
import kotlinx.coroutines.reactor.mono
import reactor.util.Loggers

internal object CentralModalEventHandler {
    internal val listeners = mutableMapOf<String, Modal>()

    private val log = Loggers.getLogger(this::class.java)

    init {
        client.on(ModalSubmitInteractionEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    private suspend fun handle(event: ModalSubmitInteractionEvent) {
        listeners.remove(event.customId)?.let {
            runCatching {
                with (event) {
                    getComponents(ActionComponent::class.java).let { components ->
                        it.collectValues(components)
                    }
                }
            }.onFailure { e ->
                return log.error("Error while collecting values for modal", e)
            }
            runCatching {
                it.handle(event)
            }.onFailure { e ->
                it.onException(event, e)
            }
        }
    }
}