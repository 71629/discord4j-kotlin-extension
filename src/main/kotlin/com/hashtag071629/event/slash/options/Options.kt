package com.hashtag071629.event.slash.options

import com.hashtag071629.annotations.DelegatedOptionMarker
import com.hashtag071629.event.slash.SlashCommandDefinition
import discord4j.core.`object`.entity.channel.Channel
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData

public fun @DelegatedOptionMarker SlashCommandDefinition.stringOption(
    name: String,
    description: String,
    minLength: Int = 0,
    maxLength: Int = 6000,
    choices: List<ApplicationCommandOptionChoiceData>? = null,
): StringOption = StringOption(name, description, minLength, maxLength, choices).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommandDefinition.channelOption(
    name: String,
    description: String,
    channelTypes: Set<Channel.Type>? = null,
): ChannelOption = ChannelOption(name, description, channelTypes).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommandDefinition.roleOption(
    name: String,
    description: String,
): RoleOption = RoleOption(name, description).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommandDefinition.userOption(
    name: String,
    description: String,
): UserOption = UserOption(name, description).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommandDefinition.attachmentOption(
    name: String,
    description: String,
    fileTypes: Set<String>? = null,
): AttachmentOption = AttachmentOption(name, description, fileTypes).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommandDefinition.longOption(
    name: String,
    description: String,
    minValue: Double = Double.MIN_VALUE,
    maxValue: Double = Double.MAX_VALUE,
): LongOption = LongOption(name, description, minValue, maxValue).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommandDefinition.doubleOption(
    name: String,
    description: String,
    minValue: Double = Double.MIN_VALUE,
    maxValue: Double = Double.MAX_VALUE,
): DoubleOption = DoubleOption(name, description, minValue, maxValue).also {
    optionDelegates.add(it)
}

public fun <T : SlashCommandOption<R>, R> @DelegatedOptionMarker SlashCommandDefinition.require(option: T) : RequiredOption<T, R> {
    optionDelegates.first { it === option }.setRequired()
    return RequiredOption(option)
}