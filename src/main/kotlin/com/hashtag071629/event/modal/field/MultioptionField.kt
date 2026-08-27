package com.hashtag071629.event.modal.field

internal sealed interface MultioptionField {
    fun choice(value: String, label: String, description: String): MultioptionChoice = MultioptionChoice(value, label, description, false)

    fun defaultChoice(value: String, label: String, description: String): MultioptionChoice = MultioptionChoice(value, label, description, true)
}