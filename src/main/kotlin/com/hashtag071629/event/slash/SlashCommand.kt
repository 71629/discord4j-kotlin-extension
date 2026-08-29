package com.hashtag071629.event.slash

import com.hashtag071629.annotations.DelegatedOptionMarker
import com.hashtag071629.event.slash.options.AttachmentOption
import com.hashtag071629.event.slash.options.ChannelOption
import com.hashtag071629.event.slash.options.DoubleOption
import com.hashtag071629.event.GatewayEvent
import com.hashtag071629.event.slash.options.LongOption
import com.hashtag071629.event.slash.options.RequiredOption
import com.hashtag071629.event.slash.options.RoleOption
import com.hashtag071629.event.slash.options.SlashCommandOption
import com.hashtag071629.event.slash.options.StringOption
import com.hashtag071629.event.slash.options.UserOption
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.command.ApplicationIntegrationType
import discord4j.core.`object`.entity.channel.Channel
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.rest.util.PermissionSet
import kotlinx.coroutines.reactor.mono
import reactor.util.Logger
import reactor.util.Loggers

@DelegatedOptionMarker
public abstract class SlashCommand : GatewayEvent<ChatInputInteractionEvent>() {
    public abstract val name: String
    public abstract val description: String

    public open val defaultMemberPermissions: PermissionSet? = null
    public open val nsfw: Boolean = false
    public open val integrationTypes: Set<ApplicationIntegrationType>? = null

    private val optionDelegates = mutableSetOf<SlashCommandOption<*>>()

    override suspend fun onException(event: ChatInputInteractionEvent, e: Throwable) {
        e.printStackTrace()
    }

    internal fun toApplicationCommandRequest(): ApplicationCommandRequest = ApplicationCommandRequest.builder().apply {
        type(1)
        name(name)
        description(description)
        addAllOptions(optionDelegates.map { it.optionData.build() })
        defaultMemberPermissions?.let { defaultMemberPermissions(it.rawValue.toString()) }
        nsfw(nsfw)
        integrationTypes?.let { integrationTypes(it.map { i -> i.value }) }
    }.build()

    protected fun stringOption(
        name: String,
        description: String,
        minLength: Int = 0,
        maxLength: Int = 6000,
        choices: List<ApplicationCommandOptionChoiceData>? = null,
    ): StringOption = StringOption(name, description, minLength, maxLength, choices).also {
        optionDelegates.add(it)
    }

    protected fun channelOption(
        name: String,
        description: String,
        channelTypes: Set<Channel.Type>? = null,
    ): ChannelOption = ChannelOption(name, description, channelTypes).also {
        optionDelegates.add(it)
    }

    protected fun roleOption(
        name: String,
        description: String,
    ): RoleOption = RoleOption(name, description).also {
        optionDelegates.add(it)
    }

    protected fun userOption(
        name: String,
        description: String,
    ): UserOption = UserOption(name, description).also {
        optionDelegates.add(it)
    }

    protected fun attachmentOption(
        name: String,
        description: String,
        fileTypes: Set<String>? = null,
    ): AttachmentOption = AttachmentOption(name, description, fileTypes).also {
        optionDelegates.add(it)
    }

    protected fun longOption(
        name: String,
        description: String,
        minValue: Double = Double.MIN_VALUE,
        maxValue: Double = Double.MAX_VALUE,
    ): LongOption = LongOption(name, description, minValue, maxValue).also {
        optionDelegates.add(it)
    }

    protected fun doubleOption(
        name: String,
        description: String,
        minValue: Double = Double.MIN_VALUE,
        maxValue: Double = Double.MAX_VALUE,
    ): DoubleOption = DoubleOption(name, description, minValue, maxValue).also {
        optionDelegates.add(it)
    }

    protected fun <T : SlashCommandOption<R>, R> require(option: T) : RequiredOption<T, R> {
        optionDelegates.first { it === option }.setRequired()
        return RequiredOption(option)
    }

    public companion object {
        private val listeners: MutableSet<SlashCommand> = mutableSetOf()
        protected val log: Logger = Loggers.getLogger(this::class.java)

        private suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
            listeners.firstOrNull { event.commandName == it.name }?.let {
                with(it) {
                    runCatching {
                        it.optionDelegates.forEach { o -> o.onSlashCommand(event) }
                        handle(event)
                    }.onFailure { e ->
                        onException(event, e)
                    }
                }
            } ?: log.error("Slash command not found: ${event.commandName}")
        }

        public fun GatewayDiscordClient.slashCommand(config: SlashCommandConfigurator.() -> Unit) {
            SlashCommandConfigurator().apply(config).also {
                it.configure()
                listeners.addAll(it.commands)
            }
            on(ChatInputInteractionEvent::class.java) { mono { onSlashCommand(it) } }.subscribe {  }
        }
    }
}