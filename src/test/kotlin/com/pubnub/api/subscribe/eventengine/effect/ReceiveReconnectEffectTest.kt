package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors

class ReceiveReconnectEffectTest : BaseEffectTest() {
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val reason = PubNubException("Unknown error")
    private val attempts = 1
    private val eventSink = TestEventSink()
    private val retryPolicy = LinearPolicy(fixedDelay = Duration.ofMillis(10))
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val messages: List<PNEvent> = createMessages()
    private val receiveMessageResult = ReceiveMessagesResult(messages, subscriptionCursor)

    @Test
    fun `should deliver ReceiveReconnectSuccess event when ReceiveReconnectEffect succeeded`() {
        // given
        val receiveReconnectEffect = ReceiveReconnectEffect(
            successfulRemoteAction(receiveMessageResult),
            eventSink,
            retryPolicy,
            executorService,
            attempts,
            reason
        )

        // when
        receiveReconnectEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(
                    listOf(Event.ReceiveReconnectSuccess(messages, subscriptionCursor)),
                    eventSink.events
                )
            }
    }

    @Test
    fun `should deliver ReceiveReconnectFailure event when ReceiveReconnectEffect failed`() {
        // given
        val receiveReconnectEffect = ReceiveReconnectEffect(
            failingRemoteAction(reason),
            eventSink,
            retryPolicy,
            executorService,
            attempts,
            reason
        )

        // when
        receiveReconnectEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted { assertEquals(listOf(Event.ReceiveReconnectFailure(reason)), eventSink.events) }
    }

    @Test
    fun `should deliver ReceiveReconnectGiveUp event when delay is null`() {
        // given
        val retryPolicy = NoRetriesPolicy
        val receiveReconnectEffect = ReceiveReconnectEffect(
            successfulRemoteAction(receiveMessageResult),
            eventSink,
            retryPolicy,
            executorService,
            attempts,
            reason
        )

        // when
        receiveReconnectEffect.runEffect()

        // then
        assertEquals(listOf(Event.ReceiveReconnectGiveUp(reason)), eventSink.events)
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction: RemoteAction<ReceiveMessagesResult> = mockk()
        every { remoteAction.silentCancel() } returns Unit
        val receiveReconnectEffect = ReceiveReconnectEffect(
            remoteAction,
            eventSink,
            retryPolicy,
            executorService,
            attempts,
            reason
        )

        // when
        receiveReconnectEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }
}
