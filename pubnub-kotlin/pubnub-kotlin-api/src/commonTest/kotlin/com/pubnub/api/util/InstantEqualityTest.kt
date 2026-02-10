package com.pubnub.api.util

import com.pubnub.api.utils.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class InstantEqualityTest {

    @Test
    fun equalsAndHashCode_areValueBased() {
        val a1 = Instant.fromEpochSeconds(1, 2)
        val a2 = Instant.fromEpochSeconds(1, 2)
        val b = Instant.fromEpochSeconds(1, 3)

        assertEquals(a1, a2)
        assertEquals(a1.hashCode(), a2.hashCode())

        assertNotEquals(a1, b)
    }
}
