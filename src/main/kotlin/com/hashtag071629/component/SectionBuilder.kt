package com.hashtag071629.component

import discord4j.core.`object`.component.IAccessoryComponent
import discord4j.core.`object`.component.ICanBeUsedInSectionComponent
import discord4j.core.`object`.component.Section
import discord4j.core.`object`.component.TextDisplay

public class SectionBuilder internal constructor(private val accessory: IAccessoryComponent) {
    internal val children = mutableListOf<ICanBeUsedInSectionComponent>()

    public operator fun String.unaryPlus() {
        children.add(TextDisplay.of(this))
    }

    internal fun buildAll(): Section {
        return Section.of(accessory)
    }
}