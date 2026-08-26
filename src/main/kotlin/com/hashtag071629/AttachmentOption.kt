package com.hashtag071629

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.entity.Attachment
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlin.jvm.optionals.getOrNull

public class AttachmentOption internal constructor(name: String, description: String, fileTypes: Set<String>?) : SlashCommandOption<Attachment>(name, description) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        type(11)
        name(name)
        description(description)
        fileTypes?.let { fileTypes(it) }
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsAttachment(name).getOrNull()
    }
}