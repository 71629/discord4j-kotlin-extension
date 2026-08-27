package com.hashtag071629.event.modal.field

import discord4j.common.util.Snowflake
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent
import discord4j.core.`object`.component.SelectMenu
import discord4j.core.`object`.entity.Role
import kotlinx.coroutines.reactor.awaitSingle
import kotlin.jvm.optionals.getOrNull

public class RoleSelect internal constructor(
    customId: String,
    defaultValues: List<Role> = emptyList(),
    fieldName: String = customId,
    description: String? = null,
    placeholder: String? = null,
    minValues: Int = 1,
    maxValues: Int = 1,
) : SelectField<Role>(customId, defaultValues, fieldName, description, placeholder, minValues, maxValues) {

    override fun getSelectMenu(): SelectMenu {
        return SelectMenu.ofRole(customId)
            .withDefaultValues(defaultValue?.map { SelectMenu.DefaultValue.of(it.id, SelectMenu.DefaultValue.Type.ROLE) } ?: emptyList())
    }

    context(event: ModalSubmitInteractionEvent)
    override suspend fun updateValue(component: SelectMenu) {
        val guild = event.interaction.guild.awaitSingle()
        userValue = component.values.getOrNull()?.map { guild.getRoleById(Snowflake.of(it)).awaitSingle() }
    }
}