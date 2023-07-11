package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.spyk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors

class HandshakeEffectTest {
    private val eventSink = TestEventSink()
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val reason = PubNubException("Unknown error")

    @Test
    fun `should deliver HandshakeSuccess event when HandshakeEffect succeeded`() {
        // given
        val handshakeEffect = HandshakeEffect(successfulRemoteAction(subscriptionCursor), eventSink)

        // when
        handshakeEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(Event.HandshakeSuccess(subscriptionCursor)), eventSink.events)
            }
    }

    @Test
    fun `should deliver HandshakeFailure event when HandshakeEffect failed`() {
        // given
        val handshakeEffect = HandshakeEffect(failingRemoteAction(reason), eventSink)

        // when
        handshakeEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(Event.HandshakeFailure(reason)), eventSink.events)
            }
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction: RemoteAction<SubscriptionCursor> = spyk()
        val handshakeEffect = HandshakeEffect(remoteAction, eventSink)

        // when
        handshakeEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }
}

class TestEventSink : Sink<Event> {
    val events = mutableListOf<Event>()

    override fun add(item: Event) {
        events.add(item)
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
