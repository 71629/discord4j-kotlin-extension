package com.hashtag071629

import discord4j.core.DiscordClient
import discord4j.core.GatewayDiscordClient
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.gateway.intent.IntentSet
import kotlinx.coroutines.reactive.awaitSingle

public lateinit var client: GatewayDiscordClient

public suspend fun initializeClient(token: String, intents: IntentSet): GatewayDiscordClient {
    return DiscordClient
        .create(token)
        .gateway()
        .setEnabledIntents(intents)
        .login()
        .awaitSingle()
}

public fun GatewayDiscordClient.withClient(action: (GatewayDiscordClient) -> Unit) {
    client = this
    action(this)
    onDisconnect().block()
}

internal val defaultApplicationCommandRequest = ApplicationCommandRequest.builder()