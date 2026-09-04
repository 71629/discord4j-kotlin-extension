package com.hashtag071629.event.message

import discord4j.core.event.domain.message.MessageCreateEvent

public class MessageCreateListenerDefinition internal constructor() : MessageListenerDefinition<MessageCreateEvent>() {
    public fun onReceive(block: suspend (MessageCreateEvent) -> Unit) {
        action = block
    }
}