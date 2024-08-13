package com.pubnub.api.utils
import kotlin.jvm.JvmStatic

/**
 * An optional that accepts nullable values. Thus, it can represent two (`PatchValue<T>`) or three (`PatchValue<T>?`) states:
 * * `PatchValue.of(someValue)` - value is present and that value is `someValue`
 * * `PatchValue.of(null)` - value is present and that value is `null`
 * * `null` - lack of information about value (no update for this field)
 */
data class PatchValue<out T> internal constructor(val value: T) {
    companion object {
        /**
         * Create an optional with the specified value (which can be null).
         */
        @JvmStatic
        fun <T> of(value: T): PatchValue<T> = PatchValue(value)
    }
}
