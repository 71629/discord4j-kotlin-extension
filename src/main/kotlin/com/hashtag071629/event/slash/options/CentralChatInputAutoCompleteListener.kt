package com.hashtag071629.event.slash.options

import com.hashtag071629.client
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent
import kotlinx.coroutines.reactor.mono

internal object CentralChatInputAutoCompleteListener {
    internal val listeners = mutableSetOf<AutoCompleteOption<*>>()

    init {
        client.on(ChatInputAutoCompleteEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    private suspend fun handle(event: ChatInputAutoCompleteEvent) {
        runCatching {
            listeners.filter { it.name == event.focusedOption.name }
                .takeIf { it.isNotEmpty() }
                ?.firstOrNull { it.parent.name == event.commandName }
                ?.let {
                    event.respondWithSuggestions(it.provider.provide(event)).subscribe()
                }
        }.onFailure { it.printStackTrace() }
    }
}