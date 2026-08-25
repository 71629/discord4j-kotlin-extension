package com.hashtag071629

import discord4j.core.DiscordClient
import discord4j.core.GatewayDiscordClient
import discord4j.gateway.intent.IntentSet
import kotlinx.coroutines.reactive.awaitSingle

public lateinit var client: GatewayDiscordClient

public suspend fun client(token: String, intents: IntentSet, action: suspend GatewayDiscordClient.() -> Unit) {
    client = DiscordClient
        .create(token)
        .gateway()
        .setEnabledIntents(intents)
        .login()
        .awaitSingle()

    client.action()
}