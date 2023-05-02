package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RemoteActionManagedEffectTest {
    @Test
    fun `failing handshake runs the onCompletion block`() {
        // given
        val latch = CountDownLatch(2)
        val failingManagedEffect = failingRemoteAction<Int>().toManagedEffect { _, s ->
            if (s.error) {
                latch.countDown()
            }
        }

        // when
        failingManagedEffect.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
    }

    @Test
    fun `succeeding handshake runs the onCompletion block`() {
        // given
        val latch = CountDownLatch(2)
        val failingManagedEffect = successfulRemoteAction(42).toManagedEffect { _, s ->
            if (!s.error) {
                latch.countDown()
            }
        }

        // when
        failingManagedEffect.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
    }
}

fun <T> successfulRemoteAction(result: T): RemoteAction<T> = object : RemoteAction<T> {
    private val executors = Executors.newSingleThreadExecutor()

    override fun sync(): T? {
        throw PubNubException("Sync not supported")
    }

    override fun silentCancel() {
    }

    override fun async(callback: (result: T?, status: PNStatus) -> Unit) {
        executors.submit {
            callback(
                result,
                PNStatus(
                    PNStatusCategory.PNAcknowledgmentCategory,
                    error = false,
                    operation = PNOperationType.PNSubscribeOperation
                )
            )
        }
    }
}

fun <T> failingRemoteAction(exception: PubNubException = PubNubException("Exception")): RemoteAction<T> =
    object : RemoteAction<T> {
        private val executors = Executors.newSingleThreadExecutor()

        override fun sync(): T? {
            throw PubNubException("Sync not supported")
        }

        override fun silentCancel() {
        }

        override fun async(callback: (result: T?, status: PNStatus) -> Unit) {
            executors.submit {
                callback(
                    null,
                    PNStatus(
                        PNStatusCategory.PNUnknownCategory,
                        error = true,
                        exception = exception,
                        operation = PNOperationType.PNSubscribeOperation
                    )
                )
            }
        }
    }
