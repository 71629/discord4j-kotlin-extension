package com.hashtag071629.component

import discord4j.core.`object`.component.SelectMenu

public class StringSelectBuilder internal constructor(customId: String) : SelectMenuBuilder(customId) {
    private val choices: MutableList<MultioptionChoice> = mutableListOf()

    public fun choice(label: String, value: String, description: String? = null) {
        choices.add(MultioptionChoice(label, value, description))
    }

    public fun default(label: String, value: String, description: String? = null) {
        choices.add(MultioptionChoice(label, value, description))
    }

    internal fun buildAll(): SelectMenu {
        val selectMenu = SelectMenu.of(customId, choices.map {
            val option = SelectMenu.Option.of(it.label, it.value).withDefault(it.isDefault)
            it.description?.let { d -> option.withDescription(d) } ?: option
        })

        return super.buildAll(selectMenu)
    }
}