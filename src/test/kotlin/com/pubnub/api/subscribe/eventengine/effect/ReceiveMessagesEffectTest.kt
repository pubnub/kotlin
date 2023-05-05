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

class ReceiveMessagesEffectTest : BaseEffectTest() {
    private val channels = listOf("channel1")
    private val channelGroups = listOf("channelGroup1")
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val receiveMessagesProvider = mockk<ReceiveMessagesProvider>()
    private val eventDeliver = SubscribeManagedEffectFactoryTest.TestEventDeliver()
    private val receiveMessagesInvocation =
        SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
    private val messages: List<PNEvent> = createMessages()
    private val receiveMessageResult = ReceiveMessagesResult(messages, subscriptionCursor)
    private val reason = PubNubException("Test")

    @Test
    fun `should deliver ReceiveSuccess event when ReceiveMessagesEffect succeeded `() {
        // given
        every {
            receiveMessagesProvider.receiveMessages(
                channels,
                channelGroups,
                subscriptionCursor
            )
        } returns successfulRemoteAction(receiveMessageResult)
        val receiveMessagesEffect =
            ReceiveMessagesEffect(receiveMessagesProvider, eventDeliver, receiveMessagesInvocation)

        // when
        receiveMessagesEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.ReceiveSuccess(messages, subscriptionCursor)), eventDeliver.events)
    }

    @Test
    fun `should deliver ReceiveFailure event when ReceiveMessagesEffect failed `() {
        // given
        every {
            receiveMessagesProvider.receiveMessages(
                channels,
                channelGroups,
                subscriptionCursor
            )
        } returns failingRemoteAction(reason)
        val receiveMessagesEffect =
            ReceiveMessagesEffect(receiveMessagesProvider, eventDeliver, receiveMessagesInvocation)

        // when
        receiveMessagesEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.ReceiveFailure(reason)), eventDeliver.events)
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction = mockk<RemoteAction<ReceiveMessagesResult>>()
        every {
            receiveMessagesProvider.receiveMessages(
                channels,
                channelGroups,
                subscriptionCursor
            )
        } returns remoteAction
        every { remoteAction.silentCancel() } returns Unit
        val receiveMessagesEffect =
            ReceiveMessagesEffect(receiveMessagesProvider, eventDeliver, receiveMessagesInvocation)

        // when
        receiveMessagesEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }

    @Test
    fun `should execute completionBock when such block is provided`() {
        // given
        every {
            receiveMessagesProvider.receiveMessages(
                channels,
                channelGroups,
                subscriptionCursor
            )
        } returns successfulRemoteAction(receiveMessageResult)
        val receiveMessagesEffect =
            ReceiveMessagesEffect(receiveMessagesProvider, eventDeliver, receiveMessagesInvocation)
        val completionBlock: () -> Unit = mockk()
        every { completionBlock() } returns Unit

        // when
        receiveMessagesEffect.runEffect(completionBlock)

        // then
        Thread.sleep(50)
        verify(exactly = 1) { completionBlock.invoke() }
    }
}
