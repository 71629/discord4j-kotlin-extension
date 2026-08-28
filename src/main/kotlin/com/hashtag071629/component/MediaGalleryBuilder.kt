package com.hashtag071629.component

import discord4j.core.`object`.component.MediaGallery
import discord4j.core.`object`.component.MediaGalleryItem
import discord4j.core.`object`.component.UnfurledMediaItem

public class MediaGalleryBuilder internal constructor() : ComponentDsl() {
    private val items = mutableListOf<MediaGalleryItem>()

    public fun item(mediaItem: UnfurledMediaItem, description: String? = null, spoiler: Boolean = false) {
        description?.let { items.add(MediaGalleryItem.of(mediaItem, it, spoiler)) }
            ?: items.add(MediaGalleryItem.of(mediaItem, spoiler))
    }

    internal fun buildAll(): MediaGallery {
        return MediaGallery.of(items)
    }
}