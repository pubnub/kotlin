package com.pubnub.kmp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PNFutureTest {

    @Test
    fun asFuture1() {
        val value = true
        var set = false
        value.asFuture().async { result ->
            assertTrue { result.isSuccess }
            result.onSuccess { value ->
                set = value
            }
        }
        assertEquals(value, set)
    }

    @Test
    fun then() {
        val value = true
        var set: String? = null
        value.asFuture().then { it.toString() }.async { result ->
            assertTrue { result.isSuccess }
            result.onSuccess { value ->
                set = value
            }
        }
        assertEquals(value.toString(), set)
    }

    @Test
    fun thenAsync() {
        val value = true
        var set: String? = null
        value.asFuture().thenAsync { it.toString().asFuture() }.async { result ->
            assertTrue { result.isSuccess }
            result.onSuccess { value ->
                set = value
            }
        }
        assertEquals(value.toString(), set)
    }
}