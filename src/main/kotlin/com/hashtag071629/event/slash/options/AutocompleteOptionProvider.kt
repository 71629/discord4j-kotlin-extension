package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData

public fun interface AutocompleteOptionProvider {
    public suspend fun provide(event: ChatInputAutoCompleteEvent): List<ApplicationCommandOptionChoiceData>
}