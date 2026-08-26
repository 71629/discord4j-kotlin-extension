package com.hashtag071629

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.entity.User
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlinx.coroutines.reactor.awaitSingleOrNull

public class UserOption internal constructor(name: String, description: String) : SlashCommandOption<User>(name, description) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        type(6)
        name(name)
        description(description)
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsUser(name).awaitSingleOrNull()
    }
}