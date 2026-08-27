package com.hashtag071629.event.modal.field

import com.hashtag071629.client
import discord4j.common.util.Snowflake
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.SelectMenu
import discord4j.core.`object`.entity.User
import kotlinx.coroutines.reactor.awaitSingle
import kotlin.jvm.optionals.getOrNull

public class UserSelect internal constructor(
    customId: String,
    defaultValues: List<User> = emptyList(),
    fieldName: String = customId,
    description: String? = null,
    placeholder: String? = null,
    minValues: Int = 1,
    maxValues: Int = 1,
) : SelectField<User>(customId, defaultValues, fieldName, description, placeholder, minValues, maxValues) {
    override fun getSelectMenu(): SelectMenu {
        return SelectMenu.ofUser(customId)
            .withDefaultValues(defaultValue?.map { SelectMenu.DefaultValue.of(it.id, SelectMenu.DefaultValue.Type.USER) } ?: emptyList())
    }

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: SelectMenu) {
        userValue = component.values.getOrNull()?.map { client.getUserById(Snowflake.of(it)).awaitSingle() }
    }
}