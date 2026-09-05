package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlin.jvm.optionals.getOrNull

public class AutocompleteLongOption internal constructor(name: String, description: String, minValue: Long?, maxValue: Long?, provider: AutocompleteOptionProvider) : AutoCompleteOption<Long>(name, description, provider) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = super.optionData.apply {
        type(4)
        minValue?.let { minValue(it.toDouble()) }
        maxValue?.let { maxValue(it.toDouble()) }
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsLong(name).getOrNull()
    }
}