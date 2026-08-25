package com.hashtag071629

import com.hashtag071629.SlashCommand.Companion.listeners
import com.hashtag071629.SlashCommand.Companion.log
import com.hashtag071629.SlashCommand.Companion.onSlashCommand
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.command.ApplicationIntegrationType
import discord4j.core.`object`.entity.Attachment
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.entity.channel.Channel
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData
import discord4j.rest.util.PermissionSet
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.jspecify.annotations.NonNull
import reactor.util.Logger
import reactor.util.Loggers
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KProperty

public abstract class SlashCommand : GatewayEvent<ChatInputInteractionEvent>() {
    public abstract val name: String
    public abstract val description: String

    public open val defaultMemberPermissions: PermissionSet? = null
    public open val nsfw: Boolean = false
    public open val integrationTypes: Set<ApplicationIntegrationType>? = null

    private val optionDelegates = mutableSetOf<SlashCommandOption<*>>()

    override suspend fun ChatInputInteractionEvent.onException(e: Throwable) {
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
                        event.handle()
                    }.onFailure { e ->
                        event.onException(e)
                    }
                }
            } ?: log.error("Slash command not found: ${event.commandName}")
        }

        public suspend fun GatewayDiscordClient.slashCommand(config: SlashCommandConfigurator.() -> Unit) {
            SlashCommandConfigurator().apply(config).also {
                it.configure()
                listeners.addAll(it.commands)
            }
            on(ChatInputInteractionEvent::class.java) { mono { onSlashCommand(it) } }.subscribe {  }
        }
    }
}

public class SlashCommandConfigurator internal constructor() {
    internal val commands = mutableSetOf<SlashCommand>()

    public fun install(command: SlashCommand) {
        commands.add(command)
    }

    internal suspend fun configure() {
        val requests = commands.map { it.toApplicationCommandRequest() }
        val applicationId = client.restClient.applicationId.awaitSingle()
        client.restClient.applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, requests).subscribe {
            log.info("Installed Slash Command: ${it.name()}")
        }
    }

    private companion object {
        private val log = Loggers.getLogger(SlashCommandConfigurator::class.java)
    }
}

public abstract class SlashCommandOption<T> internal constructor(public val name: String, public val description: String) {
    protected var userChoice: T? = null
    internal abstract val optionData: ImmutableApplicationCommandOptionData.Builder

    public operator fun getValue(thisRef: SlashCommand, property: KProperty<*>): T? = userChoice

    internal abstract suspend fun onSlashCommand(event: ChatInputInteractionEvent)

    public fun setRequired() {
        optionData.required(true)
    }
}

public class StringOption internal constructor(name: String, description: String, minLength: Int, maxLength: Int, choices: List<ApplicationCommandOptionChoiceData>?) : SlashCommandOption<String>(name, description) {
    init {
        require(minLength in 0..6000) { "minLength must be between 0 and 6000" }
        require(maxLength in 0..6000) { "maxLength must be between 0 and 6000" }
    }

    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        type(3)
        name(name)
        description(description)
        minLength(minLength)
        maxLength(maxLength)
        choices?.let { choices(it) }
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsString(name).getOrNull()
    }
}

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

public class LongOption internal constructor(name: String, description: String, minValue: Double, maxValue: Double) : SlashCommandOption<Long>(name, description) {
    init {
        require(minValue <= maxValue) { "minValue must be less than or equal to maxValue" }
    }

    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        type(4)
        name(name)
        description(description)
        minValue(minValue)
        maxValue(maxValue)
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsLong(name).getOrNull()
    }
}

public class DoubleOption internal constructor(name: String, description: String, minValue: Double, maxValue: Double) : SlashCommandOption<Double>(name, description) {
    override val optionData: ImmutableApplicationCommandOptionData.Builder = ImmutableApplicationCommandOptionData.builder().apply {
        type(10)
        name(name)
        description(description)
        minValue(minValue)
        maxValue(maxValue)
    }

    override suspend fun onSlashCommand(event: ChatInputInteractionEvent) {
        userChoice = event.getOptionAsDouble(name).getOrNull()
    }
}

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

public class RequiredOption<T : SlashCommandOption<R>, R> internal constructor(private val option: T) {
    public operator fun getValue(thisRef: SlashCommand, property: KProperty<*>): @NonNull R {
        return requireNotNull(option.getValue(thisRef, property))
    }
}
