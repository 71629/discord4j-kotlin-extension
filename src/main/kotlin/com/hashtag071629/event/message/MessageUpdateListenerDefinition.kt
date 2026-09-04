package com.hashtag071629.event.message

import discord4j.core.event.domain.message.MessageUpdateEvent

public class MessageUpdateListenerDefinition internal constructor() : MessageListenerDefinition<MessageUpdateEvent>() {
    public fun onEdit(block: suspend (MessageUpdateEvent) -> Unit) {
        action = block
    }
}