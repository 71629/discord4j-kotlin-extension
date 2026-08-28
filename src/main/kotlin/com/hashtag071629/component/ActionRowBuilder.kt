package com.hashtag071629.component

import discord4j.core.`object`.component.ActionComponent
import discord4j.core.`object`.component.ActionRow

public class ActionRowBuilder internal constructor() : ComponentDsl() {
    private val children: MutableList<ActionComponent> = mutableListOf()

    public fun button(customId: String, builder: ButtonBuilder.() -> Unit) {
        ButtonBuilder(customId).apply(builder).buildAll()
    }

    public fun stringSelect(customId: String, builder: StringSelectBuilder.() -> Unit) {
        StringSelectBuilder(customId).apply(builder).buildAll()
    }

    public fun userSelect(customId: String, builder: UserSelectBuilder.() -> Unit) {
        UserSelectBuilder(customId).apply(builder).buildAll()
    }

    public fun roleSelect(customId: String, builder: RoleSelectBuilder.() -> Unit) {
        RoleSelectBuilder(customId).apply(builder).buildAll()
    }

    public fun channelSelect(customId: String, builder: ChannelSelectBuilder.() -> Unit) {
        ChannelSelectBuilder(customId).apply(builder).buildAll()
    }

    internal fun buildAll(): ActionRow {
        return ActionRow.of(children)
    }
}