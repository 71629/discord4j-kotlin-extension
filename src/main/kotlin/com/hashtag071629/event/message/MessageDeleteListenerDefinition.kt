package com.hashtag071629.event.message

import discord4j.core.event.domain.message.MessageDeleteEvent

public class MessageDeleteListenerDefinition internal constructor() : MessageListenerDefinition<MessageDeleteEvent>() {
    public fun onDelete(block: suspend (MessageDeleteEvent) -> Unit) {
        action = block
    }
}