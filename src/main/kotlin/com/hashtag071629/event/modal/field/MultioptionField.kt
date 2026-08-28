package com.hashtag071629.event.modal.field

import com.hashtag071629.component.MultioptionChoice

internal sealed interface MultioptionField {
    fun choice(value: String, label: String, description: String): MultioptionChoice =
        MultioptionChoice(value, label, description, false)

    fun defaultChoice(value: String, label: String, description: String): MultioptionChoice =
        MultioptionChoice(value, label, description, true)
}