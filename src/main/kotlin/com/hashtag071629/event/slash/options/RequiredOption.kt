package com.hashtag071629.event.slash.options

import org.jspecify.annotations.NonNull
import kotlin.reflect.KProperty

public class RequiredOption<T : SlashCommandOption<R>, R> internal constructor(private val option: T) {
    public operator fun getValue(thisRef: Any?, property: KProperty<*>): @NonNull R {
        return requireNotNull(option.getValue(thisRef, property))
    }
}