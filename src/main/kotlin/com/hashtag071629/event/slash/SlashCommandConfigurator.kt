package com.hashtag071629.event.slash

import com.hashtag071629.annotations.ClientMarker
import com.hashtag071629.client
import kotlinx.coroutines.reactor.awaitSingle
import reactor.util.Loggers

@ClientMarker
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