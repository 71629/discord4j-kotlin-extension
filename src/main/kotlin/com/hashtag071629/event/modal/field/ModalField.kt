package com.hashtag071629.event.modal.field

import com.hashtag071629.annotations.DelegatedOptionMarker
import com.hashtag071629.event.modal.Modal
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.ActionComponent
import discord4j.core.`object`.component.ICanBeUsedInLabelComponent
import discord4j.core.`object`.component.Label
import discord4j.core.`object`.component.MessageComponent
import kotlin.reflect.KProperty

@DelegatedOptionMarker
public sealed class ModalField<R, C>(
    public var customId: String,
    public var fieldName: String,
    public var description: String?,
) where C : MessageComponent, C : ICanBeUsedInLabelComponent {
    internal abstract val component: C
    protected var userValue: R? = null

    internal val label: Label
        get() = description?.let { Label.of(fieldName, it, component) } ?: Label.of(fieldName, component)

    internal var required = false

    init {
        require(customId.length <= 100) { "Would be a BAD_REQUEST: The custom ID must have no more than 100 characters." }
    }

    @Suppress("UNCHECKED_CAST")
    context(_: ModalSubmitInteractionEvent)
    internal suspend fun collectValue(components: List<MessageComponent>) {
        components.firstOrNull { it.data.customId().get() == customId }?.let { updateValue(it as C) }
    }

    public open operator fun getValue(thisRef: Any?, property: KProperty<*>): R? = userValue

    context(event: ModalSubmitInteractionEvent)
    protected abstract suspend fun updateValue(component: C)
}
