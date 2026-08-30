package com.hashtag071629.event.modal

import com.hashtag071629.annotations.DelegatedOptionMarker
import discord4j.core.event.domain.interaction.DeferrableInteractionEvent
import discord4j.core.spec.InteractionPresentModalSpec
import java.util.UUID

public fun DeferrableInteractionEvent.presentModal(modal: Modal) {
    val customId = UUID.randomUUID().toString()

    CentralModalEventHandler.listeners[customId] = modal.also {
        presentModal(
            InteractionPresentModalSpec.builder()
                .customId(customId)
                .title(modal.title)
                .addAllComponents(it.fields.map { component -> component.label })
                .build()
        ).subscribe()
    }
}

public fun modal(builder: Modal.() -> Unit): Modal = Modal(UUID.randomUUID().toString()).apply(builder)