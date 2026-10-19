package com.pubnub.internal.java

// TEMPORARY: deliberately bad code to verify that detekt fails the build. Delete before merging.
internal class TemporaryDetektCheck(private val value: String) {
    // detekt potential-bugs > EqualsWithHashCodeExist
    override fun equals(other: Any?): Boolean = other is TemporaryDetektCheck && other.value == value
}
