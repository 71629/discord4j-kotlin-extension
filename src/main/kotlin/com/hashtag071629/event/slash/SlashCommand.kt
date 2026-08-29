package com.hashtag071629.event.slash

import com.hashtag071629.annotations.ClientMarker
import com.hashtag071629.client
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent
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

