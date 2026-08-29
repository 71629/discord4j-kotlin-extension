package com.hashtag071629.component

import com.hashtag071629.annotations.ClientMarker

@ClientMarker
public class ButtonConfigurator internal constructor() {
    public fun install(customId: String, definition: ButtonActionDefinition.() -> Unit) {
        CentralButtonEventHandler.listeners[customId] = ButtonActionDefinition(customId).apply(definition)
    }
}