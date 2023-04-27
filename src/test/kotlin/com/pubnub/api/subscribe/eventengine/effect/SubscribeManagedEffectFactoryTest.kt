package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.EventHandler
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SubscribeManagedEffectFactoryTest {

    @Test
    fun `failing handshake push HandshakeFailure event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventHandler = TestEventHandler()
        val expectedReason = PubNubException("test")
        val factory = subscribeManagedEffectFactory(
            eventHandler = testEventHandler, handshakeProvider = failingHandshakeProvider(expectedReason)
        )

        // when
        factory.create(EffectInvocation.Handshake(listOf(), listOf()))?.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.HandshakeFailure(expectedReason)), testEventHandler.events)
    }

    @Test
    fun `succeeding handshake push HandshakeSuccess event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventHandler = TestEventHandler()
        val expectedResult = SubscriptionCursor(1337L, "1337")
        val factory = subscribeManagedEffectFactory(
            eventHandler = testEventHandler, handshakeProvider = successfullHandshakeProvider(expectedResult)
        )

        // when
        factory.create(EffectInvocation.Handshake(listOf(), listOf()))?.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.HandshakeSuccess(expectedResult)), testEventHandler.events)
    }

    @Test
    fun `failing receiveMessages push ReceiveFailure event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventHandler = TestEventHandler()
        val expectedReason = PubNubException("test")
        val factory = subscribeManagedEffectFactory(
            eventHandler = testEventHandler, receiveMessagesProvider = failingReceiveMessagesProvider(expectedReason)
        )

        // when
        factory.create(EffectInvocation.ReceiveMessages(listOf(), listOf(), SubscriptionCursor(42L, "42")))
            ?.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.ReceiveFailure(expectedReason)), testEventHandler.events)
    }

    @Test
    fun `succeeding receiveMessages push ReceiveSuccess event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventHandler = TestEventHandler()
        val expectedResult = ReceiveMessagesResult(
            messages = listOf(), subscriptionCursor = SubscriptionCursor(42L, "42")
        )
        val factory = subscribeManagedEffectFactory(
            eventHandler = testEventHandler, receiveMessagesProvider = successfulReceiveMessageProvider(expectedResult)
        )

        // when
        factory.create(EffectInvocation.ReceiveMessages(listOf(), listOf(), SubscriptionCursor(42L, "42")))
            ?.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(
            listOf(
                Event.ReceiveSuccess(
                    messages = expectedResult.messages,
                    subscriptionCursor = expectedResult.subscriptionCursor
                )
            ),
            testEventHandler.events
        )
    }

    private fun failingReceiveMessagesProvider(exception: PubNubException = PubNubException("Unknown error")) =
        ReceiveMessagesProvider { _, _, _ ->
            failingRemoteAction(exception)
        }

    private fun successfulReceiveMessageProvider(
        value: ReceiveMessagesResult = ReceiveMessagesResult(
            messages = listOf(), subscriptionCursor = SubscriptionCursor(42L, "42")
        )
    ) = ReceiveMessagesProvider { _, _, _ ->
        successfullRemoteAction(value)
    }

    private fun failingHandshakeProvider(exception: PubNubException = PubNubException("Unknown error")) =
        HandshakeProvider { _, _ -> failingRemoteAction(exception) }

    private fun successfullHandshakeProvider(value: SubscriptionCursor = SubscriptionCursor(42L, "42")) =
        HandshakeProvider { _, _ -> successfullRemoteAction(value) }

    private fun subscribeManagedEffectFactory(
        eventHandler: EventHandler,
        handshakeProvider: HandshakeProvider = failingHandshakeProvider(exception = PubNubException("Unknown error")),
        receiveMessagesProvider: ReceiveMessagesProvider = failingReceiveMessagesProvider(exception = PubNubException("Unknown error"))
    ): SubscribeManagedEffectFactory = SubscribeManagedEffectFactory(
        eventHandler = eventHandler,
        handshakeProvider = handshakeProvider,
        receiveMessagesProvider = receiveMessagesProvider,
        policy = object : RetryPolicy() {
            override val maxRetries: Int = 100

            override fun computeDelay(count: Int): Duration {
                return Duration.ofMillis(10)
            }
        }
    )

    class TestEventHandler : EventHandler {
        val events = mutableListOf<Event>()

        @Synchronized
        override fun handleEvent(event: Event) {
            events.add(event)
        }
    }
}
