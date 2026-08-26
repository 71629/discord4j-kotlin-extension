package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.entity.channel.Channel
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlinx.coroutines.reactor.awaitSingleOrNull

public class ChannelOption internal constructor(name: String, description: String, channelTypes: Set<Channel.Type>?) : SlashCommandOption<Channel>(name, description) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        type(7)
        name(name)
        description(description)
        channelTypes?.let { channelTypes(it.map { c -> c.value }) }
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsChannel(name).awaitSingleOrNull()
    }
}