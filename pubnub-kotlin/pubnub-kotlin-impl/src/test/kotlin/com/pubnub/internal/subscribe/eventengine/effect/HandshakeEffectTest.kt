package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.logging.LogConfig
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.spyk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors
import java.util.function.Consumer

class HandshakeEffectTest {
    private val subscribeEventSink = TestEventSink<SubscribeEvent>()
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val reason = PubNubException("Unknown error")
    private val logConfig: LogConfig = LogConfig(pnInstanceId = "testInstanceId", userId = "testUserId")

    @Test
    fun `should deliver HandshakeSuccess event when HandshakeEffect succeeded`() {
        // given
        val handshakeEffect = HandshakeEffect(successfulRemoteAction(subscriptionCursor), subscribeEventSink, logConfig)

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
        val handshakeEffect = HandshakeEffect(failingRemoteAction(reason), subscribeEventSink, logConfig)

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
        val handshakeEffect = HandshakeEffect(remoteAction, subscribeEventSink, logConfig)

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

fun <T> successfulRemoteAction(result: T): RemoteAction<T> =
    object : RemoteAction<T> {
        private val executors = Executors.newSingleThreadExecutor()

        override fun sync(): T {
            throw PubNubException("Sync not supported")
        }

        override fun retry() {
            TODO("Not yet implemented")
        }

        override fun silentCancel() {
        }

        override fun async(callback: Consumer<Result<T>>) {
            executors.submit {
                callback.accept(Result.success(result))
            }
        }
    }

fun <T> failingRemoteAction(exception: PubNubException = PubNubException("Exception")): RemoteAction<T> =
    object : RemoteAction<T> {
        private val executors = Executors.newSingleThreadExecutor()

        override fun sync(): T {
            throw PubNubException("Sync not supported")
        }

        override fun retry() {
            TODO("Not yet implemented")
        }

        override fun silentCancel() {
        }

        override fun async(callback: Consumer<Result<T>>) {
            executors.submit {
                callback.accept(Result.failure(exception))
            }
        }
    }
