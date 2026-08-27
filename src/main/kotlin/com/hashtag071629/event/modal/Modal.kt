package com.hashtag071629.event.modal

import com.hashtag071629.annotations.DelegatedOptionMarker
import com.hashtag071629.event.GatewayEvent
import com.hashtag071629.event.modal.field.ChannelSelect
import com.hashtag071629.event.modal.field.CheckboxField
import com.hashtag071629.event.modal.field.CheckboxGroup
import com.hashtag071629.event.modal.field.FileUpload
import com.hashtag071629.event.modal.field.ModalField
import com.hashtag071629.event.modal.field.RadioGroup
import com.hashtag071629.event.modal.field.RequiredField
import com.hashtag071629.event.modal.field.RoleSelect
import com.hashtag071629.event.modal.field.TextField
import com.hashtag071629.event.modal.field.UserSelect
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.ActionComponent
import discord4j.core.`object`.component.ICanBeUsedInLabelComponent
import discord4j.core.`object`.component.MessageComponent

@DelegatedOptionMarker
public abstract class Modal : GatewayEvent<ModalSubmitInteractionEvent>() {
    context(_: ModalSubmitInteractionEvent)
    internal suspend fun collectValues(components: List<MessageComponent>) {
        fields.forEach { it.collectValue(components) }
    }

    internal val fields = mutableListOf<ModalField<*, *>>()

    protected fun textField(customId: String, config: TextField.() -> Unit): TextField =
        TextField(customId).apply(config).also { fields.add(it) }

    protected fun userSelect(customId: String, config: UserSelect.() -> Unit): UserSelect =
        UserSelect(customId).apply(config).also { fields.add(it) }

    protected fun roleSelect(customId: String, config: RoleSelect.() -> Unit): RoleSelect =
        RoleSelect(customId).apply(config).also { fields.add(it) }

    protected fun channelSelect(customId: String, config: ChannelSelect.() -> Unit): ChannelSelect =
        ChannelSelect(customId).apply(config).also { fields.add(it) }

    protected fun radioGroup(customId: String, config: RadioGroup.() -> Unit): RadioGroup =
        RadioGroup(customId).apply(config).also { fields.add(it) }

    protected fun checkboxGroup(customId: String, config: CheckboxGroup.() -> Unit): CheckboxGroup =
        CheckboxGroup(customId).apply(config).also { fields.add(it) }

    protected fun checkboxField(customId: String, config: CheckboxField.() -> Unit): CheckboxField =
        CheckboxField(customId).apply(config).also { fields.add(it) }

    protected fun fileUpload(customId: String, config: FileUpload.() -> Unit): FileUpload =
        FileUpload(customId).apply(config).also { fields.add(it) }

    protected fun <F : ModalField<R, C>, R, C> require(field: F): RequiredField<F, R, C> where C : MessageComponent, C : ICanBeUsedInLabelComponent {
        fields.first { it === field }
        return RequiredField(field)
    }
}