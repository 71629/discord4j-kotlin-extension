package com.hashtag071629

import com.hashtag071629.annotations.ClientMarker
import discord4j.core.DiscordClient
import discord4j.core.GatewayDiscordClient
import discord4j.gateway.intent.IntentSet
import kotlinx.coroutines.reactive.awaitSingle
import reactor.util.Loggers

public lateinit var client: GatewayDiscordClient private set

public fun client(token: String, intents: IntentSet, action: @ClientMarker GatewayDiscordClient.() -> Unit) {
    client = DiscordClient
        .create(token)
        .gateway()
        .setEnabledIntents(intents)
        .login()
        .block() ?: throw UnknownError()

    client.action()

    Loggers.getLogger(GatewayDiscordClient::class.java).info("Installation successful.")
}