package com.pubnub.kmp

import com.pubnub.api.v2.callbacks.Result
import kotlin.test.Test
import kotlin.test.assertContentEquals
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

    @Test
    fun awaitAll() {
        val value1 = true
        val value2 = 1
        val value3: Any? = null
        var set: List<Any?>? = null
        listOf<PNFuture<Any?>>(value1.asFuture(), value2.asFuture(), value3.asFuture()).awaitAll().async { result: Result<List<Any?>> ->
            assertTrue { result.isSuccess }
            result.onSuccess {
                set = it
            }
        }
        assertContentEquals(listOf(value1, value2, value3), set)
    }
}