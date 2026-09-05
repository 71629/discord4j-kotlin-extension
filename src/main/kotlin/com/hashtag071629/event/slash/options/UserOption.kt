package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.entity.User
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlinx.coroutines.reactor.awaitSingleOrNull

public class UserOption internal constructor(name: String, description: String) : SlashCommandOption<User>(name, description) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = super.optionData.apply {
        type(6)
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsUser(name).awaitSingleOrNull()
    }
}