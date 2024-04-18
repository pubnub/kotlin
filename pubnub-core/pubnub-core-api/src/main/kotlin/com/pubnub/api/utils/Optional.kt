package com.pubnub.api.utils

/**
 * An optional that accepts nullable values. Thus, it can represent three states:
 * * `Optional.of(someValue)` - value is present and that value is `someValue`
 * * `Optional.of(null)` - value is present and that value is `null`
 * * `Optional.none()` - no value is present
 */
sealed interface Optional<out T> {
    companion object {
        /**
         * Create an optional with the specified value (which can be null).
         */
        @JvmStatic
        fun <T> of(value: T): Optional<T> = Value(value)

        /**
         * Create an optional without a value.
         */
        @JvmStatic
        fun <T> none(): Optional<T> = Absent()

        /**
         * Convert a nullable value to an optional with the following rules:
         * * a non-null value returns an `Optional` containing the value
         * * a `null` value returns an Optional without any value.
         *
         * Please note that this is different from `Optional.of(null)`, which would return an `Optional` containing the value of `null`.
         */
        @JvmStatic
        fun <T> ofNullable(value: T): Optional<T> = value?.let { of(it) } ?: none()
    }
}

internal data class Value<T>(val value: T) : Optional<T>

internal data class Absent<T>(val value: T? = null) : Optional<T>

fun <T> Optional<T>.onValue(action: (T) -> Unit): Optional<T> = this.apply { (this as? Value<T>)?.let { action(it.value) } }

fun <T> Optional<T>.onAbsent(action: () -> Unit): Optional<T> = this.apply {
    if (this is Absent) {
        action()
    }
}
