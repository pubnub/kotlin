package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors

class ReceiveReconnectEffectTest : BaseEffectTest() {
    private val channels = listOf("channel1")
    private val channelGroups = listOf("channelGroup1")
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val reason = PubNubException("Unknown error")
    private val attempts = 1
    private val receiveMessagesProvider = mockk<ReceiveMessagesProvider>()
    private val eventDeliver = SubscribeManagedEffectFactoryTest.TestEventDeliver()
    private val retryPolicy = mockk<RetryPolicy>()
    private val receiveReconnectInvocation = SubscribeEffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, attempts, reason)
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val messages: List<PNEvent> = createMessages()
    private val receiveMessageResult = ReceiveMessagesResult(messages, subscriptionCursor)

    @Test
    fun `should deliver ReceiveReconnectSuccess event when ReceiveReconnectEffect succeeded`() {
        // given
        every { retryPolicy.nextDelay(any()) } returns Duration.ofMillis(10)
        every { receiveMessagesProvider.receiveMessages(any(), any(), any()) } returns successfulRemoteAction(receiveMessageResult)

        val receiveReconnectEffect = ReceiveReconnectEffect(
            receiveMessagesProvider,
            eventDeliver,
            retryPolicy,
            receiveReconnectInvocation,
            executorService
        )

        // when
        receiveReconnectEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.ReceiveReconnectSuccess(messages, subscriptionCursor)), eventDeliver.events)
    }

    @Test
    fun `should deliver ReceiveReconnectFailure event when ReceiveReconnectEffect failed`() {
        // given
        every { retryPolicy.nextDelay(any()) } returns Duration.ofMillis(10)
        every { receiveMessagesProvider.receiveMessages(any(), any(), any()) } returns failingRemoteAction(reason)

        val receiveReconnectEffect = ReceiveReconnectEffect(
            receiveMessagesProvider,
            eventDeliver,
            retryPolicy,
            receiveReconnectInvocation,
            executorService
        )

        // when
        receiveReconnectEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.ReceiveReconnectFailure(reason)), eventDeliver.events)
    }

    @Test
    fun `should deliver ReceiveReconnectGiveUp event when delay is null`() {
        // given
        every { retryPolicy.nextDelay(any()) } returns null
        val receiveReconnectEffect = ReceiveReconnectEffect(
            receiveMessagesProvider,
            eventDeliver,
            retryPolicy,
            receiveReconnectInvocation,
            executorService
        )

        // when
        receiveReconnectEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.ReceiveReconnectGiveUp(reason)), eventDeliver.events)
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction = mockk<RemoteAction<ReceiveMessagesResult>>()
        every { receiveMessagesProvider.receiveMessages(channels, channelGroups, subscriptionCursor) } returns remoteAction
        every { remoteAction.silentCancel() } returns Unit
        val receiveReconnectEffect = ReceiveReconnectEffect(
            receiveMessagesProvider,
            eventDeliver,
            retryPolicy,
            receiveReconnectInvocation,
            executorService
        )

        // when
        receiveReconnectEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }

    @Test
    fun `should execute completionBock when such block is provided`() {
        // given
        every { retryPolicy.nextDelay(any()) } returns Duration.ofMillis(10)
        every { receiveMessagesProvider.receiveMessages(any(), any(), any()) } returns successfulRemoteAction(receiveMessageResult)
        val receiveReconnectEffect = ReceiveReconnectEffect(
            receiveMessagesProvider,
            eventDeliver,
            retryPolicy,
            receiveReconnectInvocation,
            executorService
        )
        val completionBlockMock: () -> Unit = mockk()
        every { completionBlockMock.invoke() } returns Unit

        // when
        receiveReconnectEffect.runEffect(completionBlockMock)

        // then
        Thread.sleep(50)
        verify(exactly = 1) { completionBlockMock.invoke() }
    }
}
