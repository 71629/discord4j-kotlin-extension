package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData

public abstract class AutoCompleteOption<T> internal constructor(
    name: String,
    description: String,
    internal val provider: (ChatInputAutoCompleteEvent) -> List<ApplicationCommandOptionChoiceData>
) : SlashCommandOption<T>(name, description) {
    init {
        CentralChatInputAutoCompleteListener.listeners.add(this)
    }

    override val optionData: ImmutableApplicationCommandOptionData.Builder get() = ImmutableApplicationCommandOptionData.builder().apply {
        name(name)
        description(description)
        autocomplete(true)
    }
}