package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlin.jvm.optionals.getOrNull

public class DoubleOption internal constructor(name: String, description: String, minValue: Double, maxValue: Double) : SlashCommandOption<Double>(name, description) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        type(10)
        name(name)
        description(description)
        minValue(minValue)
        maxValue(maxValue)
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsDouble(name).getOrNull()
    }
}