package com.hashtag071629.event.modal

import com.hashtag071629.annotations.DelegatedOptionMarker
import com.hashtag071629.event.GatewayEvent
import com.hashtag071629.event.modal.field.*
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.ICanBeUsedInLabelComponent
import discord4j.core.`object`.component.MessageComponent
import reactor.util.Loggers

@DelegatedOptionMarker
public class Modal internal constructor(customId: String) : GatewayEvent<ModalSubmitInteractionEvent>() {

    public var title: String = customId
    private var onSubmit: (suspend (ModalSubmitInteractionEvent) -> Unit)? = null
    private var onException: (suspend (ModalSubmitInteractionEvent) -> Unit)? = null

    public fun onSubmit(block: suspend (ModalSubmitInteractionEvent) -> Unit) {
        onSubmit = block
    }

    public fun onException(block: suspend (ModalSubmitInteractionEvent) -> Unit) {
        onException = block
    }

    context(_: ModalSubmitInteractionEvent)
    internal suspend fun collectValues(components: List<MessageComponent>) {
        fields.forEach { it.collectValue(components) }
    }

    internal val fields = mutableListOf<ModalField<*, *>>()

    public fun textField(customId: String, config: TextField.() -> Unit): TextField =
        TextField(customId).apply(config).also { fields.add(it) }

    public fun userSelect(customId: String, config: UserSelect.() -> Unit): UserSelect =
        UserSelect(customId).apply(config).also { fields.add(it) }

    public fun roleSelect(customId: String, config: RoleSelect.() -> Unit): RoleSelect =
        RoleSelect(customId).apply(config).also { fields.add(it) }

    public fun channelSelect(customId: String, config: ChannelSelect.() -> Unit): ChannelSelect =
        ChannelSelect(customId).apply(config).also { fields.add(it) }

    public fun radioGroup(customId: String, config: RadioGroup.() -> Unit): RadioGroup =
        RadioGroup(customId).apply(config).also { fields.add(it) }

    public fun checkboxGroup(customId: String, config: CheckboxGroup.() -> Unit): CheckboxGroup =
        CheckboxGroup(customId).apply(config).also { fields.add(it) }

    public fun checkboxField(customId: String, config: CheckboxField.() -> Unit): CheckboxField =
        CheckboxField(customId).apply(config).also { fields.add(it) }

    public fun fileUpload(customId: String, config: FileUpload.() -> Unit): FileUpload =
        FileUpload(customId).apply(config).also { fields.add(it) }

    public fun <F : ModalField<R, C>, R, C> require(field: F): RequiredField<F, R, C> where C : MessageComponent, C : ICanBeUsedInLabelComponent {
        fields.first { it === field }
        return RequiredField(field)
    }

    override suspend fun handle(event: ModalSubmitInteractionEvent) {
        runCatching {
            with(event) {
                collectValues(getComponents(MessageComponent::class.java))
            }
        }.onFailure {
            return log.error("Error while collecting values for modal", it)
        }
        runCatching {
            onSubmit?.invoke(event)
        }.onFailure {
            onException?.invoke(event)
        }
    }

    private companion object {
        private val log = Loggers.getLogger(Modal::class.java)
    }
}