package com.pubnub.api.utils

sealed class OptionalChange<T> {
    class Unchanged<T> : OptionalChange<T>()
    class Removed<T> : OptionalChange<T>()
    data class Changed<T>(val value: T) : OptionalChange<T>()
}

val <T> OptionalChange<T>.value: T?
    get() {
        return when (this) {
            is OptionalChange.Unchanged -> null
            is OptionalChange.Removed -> null
            is OptionalChange.Changed -> value
        }
    }

fun <T> OptionalChange<T>.getOrDefaultOrNull(default: T?): T? {
    return when (this) {
        is OptionalChange.Unchanged -> default
        is OptionalChange.Changed -> value
        is OptionalChange.Removed -> null
    }
}
