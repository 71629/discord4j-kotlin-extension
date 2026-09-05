package com.hashtag071629.event.slash.options

import com.hashtag071629.client
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent
import kotlinx.coroutines.reactor.mono

internal object CentralChatInputAutoCompleteListener {
    internal val listeners = mutableSetOf<AutoCompleteOption<*>>()

    init {
        client.on(ChatInputAutoCompleteEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    private fun handle(event: ChatInputAutoCompleteEvent) {
        val match = listeners.firstOrNull { it.name == event.focusedOption.name } ?: return
        event.respondWithSuggestions(match.provider(event)).subscribe()
    }
}