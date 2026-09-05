package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlin.jvm.optionals.getOrNull

public class LongOption internal constructor(name: String, description: String, minValue: Long?, maxValue: Long?, choices: List<ApplicationCommandOptionChoiceData>?) : SlashCommandOption<Long>(name, description) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = super.optionData.apply {
        type(4)
        minValue?.let { minValue(it.toDouble()) }
        maxValue?.let { maxValue(it.toDouble()) }
        choices?.let { choices(choices) }
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsLong(name).getOrNull()
    }
}