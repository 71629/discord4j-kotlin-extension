package com.hashtag071629.event.modal.field

import com.hashtag071629.event.modal.Modal
import discord4j.core.`object`.component.ICanBeUsedInLabelComponent
import discord4j.core.`object`.component.MessageComponent
import org.jspecify.annotations.NonNull
import kotlin.reflect.KProperty

public class RequiredField<F : ModalField<R, C>, R, C> internal constructor(
    private val field: F
) where C : MessageComponent, C : ICanBeUsedInLabelComponent {
    public operator fun getValue(thisRef: Modal, property: KProperty<*>): @NonNull R = requireNotNull(field.getValue(thisRef, property))
}