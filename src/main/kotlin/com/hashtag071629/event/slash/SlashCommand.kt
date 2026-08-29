package com.hashtag071629.event.slash

import com.hashtag071629.annotations.ClientMarker
import com.hashtag071629.annotations.DelegatedOptionMarker
import com.hashtag071629.client
import com.hashtag071629.event.GatewayEvent
import com.hashtag071629.event.slash.options.SlashCommandOption
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.command.ApplicationIntegrationType
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.rest.util.PermissionSet
import kotlinx.coroutines.reactor.mono

@ClientMarker
public object SlashCommand {
    internal val commands = mutableMapOf<String, SlashCommandDefinition>()

    public fun install(config: SlashCommandDefinition.() -> Unit) {
        val handler = SlashCommandDefinition().apply(config)
        commands[handler.name] = handler
    }

    public fun GatewayDiscordClient.slashCommand(config: SlashCommand.() -> Unit) {
        SlashCommand.apply(config).configure()
        on(ChatInputInteractionEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    internal fun configure() {
        val requests = commands.values.map { it.toApplicationCommandRequest() }
        val applicationId = client.restClient.applicationId.block() ?: throw UnknownError()
        client.restClient.applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, requests).subscribe()
    }

    private suspend fun handle(event: ChatInputInteractionEvent) {
        commands[event.commandName]?.handle(event)
    }
}

public class SlashCommandDefinition internal constructor() : GatewayEvent<ChatInputInteractionEvent>() {
    public lateinit var name: String
    public lateinit var description: String

    public var defaultMemberPermissions: PermissionSet? = null
    public var nsfw: Boolean = false
    public var integrationTypes: Set<ApplicationIntegrationType>? = null

    internal var onInteraction: suspend (ChatInputInteractionEvent) -> Unit = {}
    internal var onException: suspend (ChatInputInteractionEvent, Throwable) -> Unit = ::onException

    internal val optionDelegates = mutableSetOf<SlashCommandOption<*>>()

    public fun onInteraction(block: suspend (ChatInputInteractionEvent) -> Unit) {
        onInteraction = block
    }

    public fun onException(block: suspend (ChatInputInteractionEvent, Throwable) -> Unit) {
        onException = block
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

    override suspend fun handle(event: ChatInputInteractionEvent) {
        runCatching {
            optionDelegates.forEach { o -> o.onSlashCommand(event) }
            onInteraction.invoke(event)
        }.onFailure {
            onException.invoke(event, it)
        }
    }
}