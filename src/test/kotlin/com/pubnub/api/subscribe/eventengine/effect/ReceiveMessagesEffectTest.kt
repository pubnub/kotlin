package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.spyk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Test
import java.time.Duration

class ReceiveMessagesEffectTest : BaseEffectTest() {
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val eventQueue = TestEventQueue()
    private val messages: List<PNEvent> = createMessages()
    private val receiveMessageResult = ReceiveMessagesResult(messages, subscriptionCursor)
    private val reason = PubNubException("Test")

    @Test
    fun `should deliver ReceiveSuccess event when ReceiveMessagesEffect succeeded `() {
        // given
        val receiveMessagesEffect = ReceiveMessagesEffect(successfulRemoteAction(receiveMessageResult), eventQueue)

        // when
        receiveMessagesEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .until { listOf(Event.ReceiveSuccess(messages, subscriptionCursor)) == eventQueue.events }
    }

    @Test
    fun `should deliver ReceiveFailure event when ReceiveMessagesEffect failed `() {
        // given
        val receiveMessagesEffect = ReceiveMessagesEffect(failingRemoteAction(reason), eventQueue)

        // when
        receiveMessagesEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .until { listOf(Event.ReceiveFailure(reason)) == eventQueue.events }
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction: RemoteAction<ReceiveMessagesResult> = spyk()
        val receiveMessagesEffect = ReceiveMessagesEffect(remoteAction, eventQueue)

        // when
        receiveMessagesEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }
}
