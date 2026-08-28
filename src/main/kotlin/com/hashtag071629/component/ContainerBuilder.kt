package com.hashtag071629.component

import discord4j.core.`object`.component.Container
import discord4j.core.`object`.component.File
import discord4j.core.`object`.component.IAccessoryComponent
import discord4j.core.`object`.component.ICanBeUsedInContainerComponent
import discord4j.core.`object`.component.Separator
import discord4j.core.`object`.component.TextDisplay
import discord4j.core.`object`.component.UnfurledMediaItem
import discord4j.rest.util.Color

public class ContainerBuilder internal constructor(private val color: Color?) : ComponentDsl() {
    internal val children = mutableListOf<ICanBeUsedInContainerComponent>()

    public operator fun String.unaryPlus() {
        children.add(TextDisplay.of(this))
    }

    public fun section(accessory: IAccessoryComponent, builder: SectionBuilder.() -> Unit) {
        children.add(SectionBuilder(accessory).apply(builder).buildAll())
    }

    public fun mediaGallery(builder: MediaGalleryBuilder.() -> Unit) {
        children.add(MediaGalleryBuilder().apply(builder).buildAll())
    }

    public fun file(file: UnfurledMediaItem, spoiler: Boolean = false) {
        children.add(File.of(file, spoiler))
    }

    public fun actionRow(builder: ActionRowBuilder.() -> Unit) {
        children.add(ActionRowBuilder().apply(builder).buildAll())
    }

    public fun separator() {
        children.add(Separator.of())
    }

    internal fun buildAll(): Container {
        return Container.of(color, children)
    }
}