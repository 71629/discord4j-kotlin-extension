package com.hashtag071629.event.slash.options

import com.hashtag071629.client
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlinx.coroutines.reactor.mono
import kotlin.jvm.optionals.getOrNull

public class AutocompleteStringOption internal constructor(
    name: String,
    description: String,
    minLength: Int,
    maxLength: Int,
    provider: suspend (ChatInputAutoCompleteEvent) -> List<ApplicationCommandOptionChoiceData>
) : AutoCompleteOption<String>(name, description, provider) {
    init {
        require(minLength in 0..6000) { "minLength must be between 0 and 6000" }
        require(maxLength in 0..6000) { "maxLength must be between 0 and 6000" }
    }

    override val optionData: ImmutableApplicationCommandOptionData.Builder = super.optionData.apply {
        type(3)
        minLength(minLength)
        maxLength(maxLength)
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsString(name).getOrNull()
    }
}