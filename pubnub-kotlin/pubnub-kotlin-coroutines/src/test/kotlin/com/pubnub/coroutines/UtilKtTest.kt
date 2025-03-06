package com.pubnub.coroutines

import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.asFuture
import com.pubnub.kmp.then
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class FakePNFuture<T>(private val block: (Consumer<com.pubnub.api.v2.callbacks.Result<T>>) -> Unit) : PNFuture<T> {
    override fun async(callback: Consumer<com.pubnub.api.v2.callbacks.Result<T>>) {
        block(callback)
    }
}

class UtilKtTest {
    @Test
    fun `await returns result on success`() = runTest {
        val expected = "Success Result"
        val future = FakePNFuture<String> { callback ->
            // Simulate a successful asynchronous result.
            callback.accept(com.pubnub.api.v2.callbacks.Result.success(expected))
        }

        // When await() is called, it should resume with the expected result.
        val result = future.await()
        assertEquals(expected, result)
    }

    @Test
    fun `await throws exception on failure`() = runTest {
        val exception = Exception("Failure occurred")
//        val future = FakePNFuture<String> { callback ->
//            // Simulate an asynchronous failure.
//            callback.accept(com.pubnub.api.v2.callbacks.Result.failure(exception))
//        }
        val future = Unit.asFuture().then { error("exception!") }
        // When await() is called, it should resume with an exception.
        val thrown = assertFailsWith<Exception> {
            future.await()
        }
        // Optionally verify the exception message.
        assertEquals(exception.message, thrown.message)
    }
}
