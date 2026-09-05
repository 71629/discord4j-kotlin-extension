package com.hashtag071629.event.slash.options

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import kotlin.reflect.KProperty

public abstract class SlashCommandOption<T> internal constructor(public val name: String, public val description: String) {
    protected var userChoice: T? = null
    internal open val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        name(name)
        description(description)
    }

    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = userChoice

    internal abstract suspend fun onSlashCommand(event: ChatInputInteractionEvent)

    public fun setRequired() {
        optionData.required(true)
    }
}
