package com.hashtag071629.component

import discord4j.core.`object`.component.TopLevelMessageComponent

public fun components(builder: ComponentBuilder.() -> Unit): List<TopLevelMessageComponent> {
    return ComponentBuilder().apply(builder).components
}