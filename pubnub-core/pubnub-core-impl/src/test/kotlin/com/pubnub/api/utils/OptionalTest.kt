package com.pubnub.api.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class OptionalTest {
    @Test
    fun `value is set and onValue delivers value`() {
        val optional = Optional.of(123)

        var value: Int? = null
        optional.onValue {
            value = it
        }.onAbsent {
            throw IllegalStateException("Value is set so onAbsent shouldn't fire!")
        }
        assertEquals(123, value)
    }

    @Test
    fun `null is set and onValue delivers null`() {
        val optional = Optional.of(null)

        var value: Int? = 123

        optional.onValue {
            value = it
        }.onAbsent {
            throw IllegalStateException("Value is set so onAbsent shouldn't fire!")
        }
        assertNull(value)
    }

    @Test
    fun `value is not set and onAbsent is called`() {
        val optional = Optional.none<Int>()

        var absentCalled = false

        optional.onValue {
            throw IllegalStateException("Value is set so onAbsent shouldn't fire!")
        }.onAbsent {
            absentCalled = true
        }
        assertTrue(absentCalled)
    }

    @Test
    fun `Optional ofNullable transforms null values to absent values`() {
        val someValue = 123
        val nullValue: Int? = null

        val valueOptional = Optional.ofNullable(someValue)
        val noneOptional = Optional.ofNullable(nullValue)

        assertEquals(Optional.of(123), valueOptional)
        assertEquals(Optional.none<Int>(), noneOptional)
    }

    @Test
    fun `test equality of value and non-value optionals`() {
        assertNotEquals(Optional.of(124), Optional.of(123))
        assertEquals(Optional.of(124), Optional.of(124))

        assertNotEquals(Optional.none<Int>(), Optional.of(123))
        assertEquals(Optional.none<Int>(), Optional.none<Int>())
    }
}
