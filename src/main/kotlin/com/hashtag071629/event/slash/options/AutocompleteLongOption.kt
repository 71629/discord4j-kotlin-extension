package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlin.jvm.optionals.getOrNull

public class AutocompleteLongOption internal constructor(name: String, description: String, minValue: Double, maxValue: Double, provider: suspend (ChatInputAutoCompleteEvent) -> List<ApplicationCommandOptionChoiceData>) : AutoCompleteOption<Long>(name, description, provider) {
    init {
        require(minValue <= maxValue) { "minValue must be less than or equal to maxValue" }
    }

    override val optionData: ImmutableApplicationCommandOptionData.Builder = super.optionData.apply {
        type(4)
        name(name)
        description(description)
        minValue(minValue)
        maxValue(maxValue)
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsLong(name).getOrNull()
    }
}