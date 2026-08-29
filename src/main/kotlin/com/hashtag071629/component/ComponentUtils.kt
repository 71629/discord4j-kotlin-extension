package com.hashtag071629.component

import discord4j.core.`object`.component.TopLevelMessageComponent

public fun components(builder: ComponentTree.() -> Unit): List<TopLevelMessageComponent> {
    return ComponentTree().apply(builder).components
}