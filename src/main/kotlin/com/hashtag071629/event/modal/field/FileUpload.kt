package com.hashtag071629.event.modal.field

import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.FileUpload
import discord4j.core.`object`.entity.Attachment
import kotlin.jvm.optionals.getOrNull

public class FileUpload internal constructor(
    customId: String,
    fieldName: String = customId,
    description: String? = null,
    public var fileTypes: List<String> = emptyList()
) : ModalField<List<Attachment>, FileUpload>(customId, fieldName, description) {
    override val component: FileUpload get() = FileUpload.of(customId).withFileTypes(fileTypes)

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: FileUpload) {
        val resolvedData = event.resolved.getOrNull() ?: return
        userValue = component.values.getOrNull()?.mapNotNull {
            resolvedData.attachments[it]
        } ?: emptyList()
    }
}