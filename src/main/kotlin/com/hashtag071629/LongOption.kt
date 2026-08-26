package com.hashtag071629

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlin.jvm.optionals.getOrNull

public class LongOption internal constructor(name: String, description: String, minValue: Double, maxValue: Double) : SlashCommandOption<Long>(name, description) {
    init {
        require(minValue <= maxValue) { "minValue must be less than or equal to maxValue" }
    }

    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
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