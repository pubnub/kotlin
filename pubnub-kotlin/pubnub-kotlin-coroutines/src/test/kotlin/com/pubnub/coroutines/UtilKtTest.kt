package com.pubnub.coroutines

import com.pubnub.kmp.asFuture
import com.pubnub.kmp.then
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class UtilKtTest {
    @Test
    fun `await returns result on success`() = runTest {
        val expected = "Success Result"
        assertEquals(expected, expected.asFuture().await())
    }

    @Test
    fun `await throws exception on failure`() = runTest {
        val exception = Exception("Failure occurred")

        val future = exception.asFuture().then { throw it }
        // When await() is called, it should resume with an exception.
        val thrown = assertFailsWith<Exception> {
            future.await()
        }
        // Optionally verify the exception message.
        assertEquals(exception.message, thrown.message)
    }
}
