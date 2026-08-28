package com.hashtag071629.component

import discord4j.common.util.Snowflake

public open class DiscordEntitySelectMenu internal constructor(customId: String) : SelectMenuBuilder(customId) {
    protected var defaultValues: MutableList<Snowflake> = mutableListOf()

    public fun default(vararg ids: Snowflake) {
        defaultValues.addAll(ids)
    }
}