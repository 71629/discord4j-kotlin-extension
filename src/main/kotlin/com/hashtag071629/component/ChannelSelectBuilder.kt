package com.hashtag071629.component

import discord4j.core.`object`.component.SelectMenu

public class ChannelSelectBuilder internal constructor(customId: String) : DiscordEntitySelectMenu(customId) {
    internal fun buildAll(): SelectMenu {
        val selectMenu = SelectMenu.ofRole(customId, defaultValues.map{
            SelectMenu.DefaultValue.of(it, SelectMenu.DefaultValue.Type.CHANNEL)
        })

        return super.buildAll(selectMenu)
    }
}