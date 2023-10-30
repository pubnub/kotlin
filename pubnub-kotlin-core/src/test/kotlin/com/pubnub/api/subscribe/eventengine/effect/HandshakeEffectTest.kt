package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.v2.callbacks.Result
import io.mockk.spyk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors

class HandshakeEffectTest {
    private val subscribeEventSink = TestEventSink<SubscribeEvent>()
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val reason = PubNubException("Unknown error")

    @Test
    fun `should deliver HandshakeSuccess event when HandshakeEffect succeeded`() {
        // given
        val handshakeEffect = HandshakeEffect(successfulRemoteAction(subscriptionCursor), subscribeEventSink)

        // when
        handshakeEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(SubscribeEvent.HandshakeSuccess(subscriptionCursor)), subscribeEventSink.events)
            }
    }

    @Test
    fun `should deliver HandshakeFailure event when HandshakeEffect failed`() {
        // given
        val handshakeEffect = HandshakeEffect(failingRemoteAction(reason), subscribeEventSink)

        // when
        handshakeEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(SubscribeEvent.HandshakeFailure(reason)), subscribeEventSink.events)
            }
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction: RemoteAction<SubscriptionCursor> = spyk()
        val handshakeEffect = HandshakeEffect(remoteAction, subscribeEventSink)

        // when
        handshakeEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }
}

class TestEventSink<T> : Sink<T> {
    val events = mutableListOf<T>()

    override fun add(item: T) {
        events.add(item)
    }
}

fun <T> successfulRemoteAction(result: T): RemoteAction<T> = object : RemoteAction<T> {
    private val executors = Executors.newSingleThreadExecutor()

    override fun sync(): T {
        throw PubNubException("Sync not supported")
    }

    override fun silentCancel() {
    }

    override fun async(callback: (result: Result<T>) -> Unit) {
        executors.submit {
            callback(Result.success(result))
        }
    }
}

fun <T> failingRemoteAction(exception: PubNubException = PubNubException("Exception")): RemoteAction<T> =
    object : RemoteAction<T> {
        private val executors = Executors.newSingleThreadExecutor()

        override fun sync(): T {
            throw PubNubException("Sync not supported")
        }

        override fun silentCancel() {
        }

        override fun async(callback: (result: Result<T>) -> Unit) {
            executors.submit {
                callback(Result.failure(exception))
            }
        }
    }
