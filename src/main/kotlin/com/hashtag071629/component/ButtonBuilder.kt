package com.hashtag071629.component

import discord4j.core.`object`.component.Button

public class ButtonBuilder internal constructor(private val customId: String) : ComponentDsl() {
    public var style: Button.Style = Button.Style.SECONDARY
    public var label: String = customId
    public var disabled: Boolean = false

    internal fun buildAll(): Button {
        return when (style) {
            Button.Style.SECONDARY -> Button.secondary(customId, label)
            Button.Style.PRIMARY -> Button.primary(customId, label)
            Button.Style.DANGER -> Button.danger(customId, label)
            Button.Style.SUCCESS -> Button.success(customId, label)
            Button.Style.LINK -> throw NotImplementedError()
            Button.Style.PREMIUM -> throw NotImplementedError()
            Button.Style.UNKNOWN -> throw NotImplementedError()
        }.disabled(disabled)
    }
}