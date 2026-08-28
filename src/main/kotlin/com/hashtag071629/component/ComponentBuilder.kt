package com.hashtag071629.component

import discord4j.core.`object`.component.File
import discord4j.core.`object`.component.IAccessoryComponent
import discord4j.core.`object`.component.Separator
import discord4j.core.`object`.component.TextDisplay
import discord4j.core.`object`.component.TopLevelMessageComponent
import discord4j.core.`object`.component.UnfurledMediaItem
import discord4j.rest.util.Color

public open class ComponentBuilder internal constructor() : ComponentDsl() {
    internal val components = mutableListOf<TopLevelMessageComponent>()

    public operator fun String.unaryPlus() {
        components.add(TextDisplay.of(this))
    }

    public fun container(color: Color? = null, builder: ContainerBuilder.() -> Unit) {
        components.add(ContainerBuilder(color).apply(builder).buildAll())
    }

    public fun section(accessory: IAccessoryComponent, builder: SectionBuilder.() -> Unit) {
        components.add(SectionBuilder(accessory).apply(builder).buildAll())
    }

    public fun mediaGallery(builder: MediaGalleryBuilder.() -> Unit) {
        components.add(MediaGalleryBuilder().apply(builder).buildAll())
    }

    public fun file(file: UnfurledMediaItem, spoiler: Boolean = false) {
        components.add(File.of(file, spoiler))
    }

    public fun actionRow(builder: ActionRowBuilder.() -> Unit) {
        components.add(ActionRowBuilder().apply(builder).buildAll())
    }

    public fun separator() {
        components.add(Separator.of())
    }

    public fun components(b: ComponentBuilder.() -> Unit): Nothing {
        throw Exception("Stub!")
    }
}