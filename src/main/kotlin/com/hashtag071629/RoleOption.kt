package com.hashtag071629

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.entity.Role
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlinx.coroutines.reactor.awaitSingleOrNull

public class RoleOption internal constructor(name: String, description: String) : SlashCommandOption<Role>(name, description) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        type(8)
        name(name)
        description(description)
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsRole(name).awaitSingleOrNull()
    }
}