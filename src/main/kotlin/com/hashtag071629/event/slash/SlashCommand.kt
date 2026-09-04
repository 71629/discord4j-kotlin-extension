package com.hashtag071629.event.slash

import com.hashtag071629.annotations.ClientMarker
import com.hashtag071629.client
import com.hashtag071629.event.message.EventListener
import com.hashtag071629.event.slash.options.SlashCommandOption
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
import discord4j.core.`object`.command.ApplicationIntegrationType
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.rest.util.PermissionSet
import kotlinx.coroutines.reactor.mono

@ClientMarker
public object SlashCommand : EventListener<ChatInputInteractionEvent, SlashCommand.Definition>() {
    override val definition: Definition get() = Definition()

    public fun GatewayDiscordClient.slashCommand(config: SlashCommand.() -> Unit) {
        apply(config).configure()
        on(ChatInputInteractionEvent::class.java) { mono { handle(it) } }.subscribe()
    }

    private fun configure() {
        val requests = listeners.map { it.toApplicationCommandRequest() }
        val applicationId = client.restClient.applicationId.block() ?: throw UnknownError()
        client.restClient.applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, requests).subscribe()
    }

    public fun Definition.onInteraction(block: suspend (ChatInputInteractionEvent) -> Unit) {
        action = block
    }

    private fun Definition.toApplicationCommandRequest() = ApplicationCommandRequest.builder().apply {
        type(1)
        name(name)
        description(description)
        addAllOptions(optionDelegates.map { it.optionData.build() })
        defaultMemberPermissions?.let { defaultMemberPermissions(it.rawValue.toString()) }
        nsfw(nsfw)
        integrationTypes?.let { integrationTypes(it.map { i -> i.value }) }
    }.build()

    override suspend fun handle(event: ChatInputInteractionEvent) {
        listeners.firstOrNull { it.predicate(event) }?.let {
            it.runCatching {
                optionDelegates.forEach { o -> o.onSlashCommand(event) }
            }
            it.handle(event)
        }
    }

    public class Definition internal constructor() : EventListener.Definition<ChatInputInteractionEvent>() {
        public lateinit var name: String
        public lateinit var description: String

        public var defaultMemberPermissions: PermissionSet? = null
        public var nsfw: Boolean = false
        public var integrationTypes: Set<ApplicationIntegrationType>? = null

        internal val optionDelegates = mutableSetOf<SlashCommandOption<*>>()
        override var predicate: (ChatInputInteractionEvent) -> Boolean = { it.commandName == name }
    }
}

