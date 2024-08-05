package com.pubnub.api.utils

/**
 * An optional that accepts nullable values. Thus, it can represent two (`PatchValue<T>`) or three (`PatchValue<T>?`) states :
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

//        /**
//         * Convert a nullable value to an optional with the following rules:
//         * * a non-null value returns a `PatchValue` containing the value
//         * * a `null` value returns a `PatchValue` without any value.
//         *
//         * Please note that this is different from `PatchValue.of(null)`, which would return an `PatchValue` containing the value of `null`.
//         */
//        internal fun <T> fromNullable(value: T): PatchValue<T> = value?.let { of(it) } ?: none()
    }
}

// internal data class Value<T>(val value: T) : PatchValue<T>
//
// internal data class Absent<T>(val value: T? = null) : PatchValue<T>
//
// fun <T> PatchValue<T>.onValue(action: (T) -> Unit): PatchValue<T> = this.apply { (this as? Value<T>)?.let { action(it.value) } }
//
// fun <T> PatchValue<T>.onAbsent(action: () -> Unit): PatchValue<T> = this.apply {
//    if (this is Absent) {
//        action()
//    }
// }
//
// val PatchValue<*>.hasValue get() = this is Value<*>
