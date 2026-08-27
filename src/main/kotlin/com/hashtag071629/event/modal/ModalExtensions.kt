package com.hashtag071629.event.modal

import discord4j.core.event.domain.interaction.DeferrableInteractionEvent
import discord4j.core.spec.InteractionPresentModalSpec
import java.util.UUID

public fun DeferrableInteractionEvent.presentModal(title: String, model: Modal) {
    val customId = UUID.randomUUID().toString()
    CentralModalEventHandler.listeners[customId] = model.also {
        presentModal(
            InteractionPresentModalSpec.builder()
                .customId(customId)
                .title(title)
                .addAllComponents(it.fields.map { component -> component.label })
                .build()
        ).subscribe()
    }
}