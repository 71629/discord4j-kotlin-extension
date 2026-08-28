package com.hashtag071629.component

import discord4j.core.`object`.component.SelectMenu

public class UserSelectBuilder internal constructor(customId: String) : DiscordEntitySelectMenu(customId) {
    internal fun buildAll(): SelectMenu {
        val selectMenu = SelectMenu.ofUser(customId, defaultValues.map{
            SelectMenu.DefaultValue.of(it, SelectMenu.DefaultValue.Type.USER)
        })

        return super.buildAll(selectMenu)
    }
}