package com.hashtag071629.event.slash.options

import com.hashtag071629.annotations.DelegatedOptionMarker
import com.hashtag071629.event.slash.SlashCommand
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent
import discord4j.core.`object`.entity.channel.Channel
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData

public fun @DelegatedOptionMarker SlashCommand.Definition.stringOption(
    name: String,
    description: String,
    minLength: Int = 0,
    maxLength: Int = 6000,
    choices: List<ApplicationCommandOptionChoiceData>? = null,
): StringOption = StringOption(name, description, minLength, maxLength, choices).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.autocompleteStringOption(
    name: String,
    description: String,
    minLength: Int = 0,
    maxLength: Int = 6000,
    provider: suspend (ChatInputAutoCompleteEvent) -> List<ApplicationCommandOptionChoiceData>,
): AutoCompleteOption<String> = AutocompleteStringOption(name, description, minLength, maxLength, provider).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.channelOption(
    name: String,
    description: String,
    channelTypes: Set<Channel.Type>? = null,
): ChannelOption = ChannelOption(name, description, channelTypes).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.roleOption(
    name: String,
    description: String,
): RoleOption = RoleOption(name, description).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.userOption(
    name: String,
    description: String,
): UserOption = UserOption(name, description).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.attachmentOption(
    name: String,
    description: String,
    fileTypes: Set<String>? = null,
): AttachmentOption = AttachmentOption(name, description, fileTypes).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.longOption(
    name: String,
    description: String,
    minValue: Double = Double.MIN_VALUE,
    maxValue: Double = Double.MAX_VALUE,
    choices: List<ApplicationCommandOptionChoiceData>? = null,
): LongOption = LongOption(name, description, minValue, maxValue, choices).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.autocompleteLongOption(
    name: String,
    description: String,
    minValue: Double = Double.MIN_VALUE,
    maxValue: Double = Double.MAX_VALUE,
    provider: suspend (ChatInputAutoCompleteEvent) -> List<ApplicationCommandOptionChoiceData>,
): AutocompleteLongOption = AutocompleteLongOption(name, description, minValue, maxValue, provider).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.doubleOption(
    name: String,
    description: String,
    minValue: Double = Double.MIN_VALUE,
    maxValue: Double = Double.MAX_VALUE,
    choices: List<ApplicationCommandOptionChoiceData>? = null,
): DoubleOption = DoubleOption(name, description, minValue, maxValue, choices).also {
    optionDelegates.add(it)
}

public fun @DelegatedOptionMarker SlashCommand.Definition.autocompleteDoubleOption(
    name: String,
    description: String,
    minValue: Double = Double.MIN_VALUE,
    maxValue: Double = Double.MAX_VALUE,
    provider: suspend (ChatInputAutoCompleteEvent) -> List<ApplicationCommandOptionChoiceData>,
): AutocompleteDoubleOption = AutocompleteDoubleOption(name, description, minValue, maxValue, provider).also {
    optionDelegates.add(it)
}

public fun <T : SlashCommandOption<R>, R> @DelegatedOptionMarker SlashCommand.Definition.require(option: T) : RequiredOption<T, R> {
    optionDelegates.first { it === option }.setRequired()
    return RequiredOption(option)
}