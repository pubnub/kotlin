package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.EventDeliver
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
        val testEventDeliver = TestEventDeliver()
        val expectedReason = PubNubException("test")
        val factory = createSubscribeManagedEffectFactory(
            eventDeliver = testEventDeliver, handshakeProvider = failingHandshakeProvider(expectedReason)
        )

        // when
        factory.create(SubscribeEffectInvocation.Handshake(listOf(), listOf()))?.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.HandshakeFailure(expectedReason)), testEventDeliver.events)
    }

    @Test
    fun `succeeding handshake push HandshakeSuccess event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventDeliver = TestEventDeliver()
        val expectedResult = SubscriptionCursor(1337L, "1337")
        val factory = createSubscribeManagedEffectFactory(
            eventDeliver = testEventDeliver, handshakeProvider = successfulHandshakeProvider(expectedResult)
        )

        // when
        factory.create(SubscribeEffectInvocation.Handshake(listOf(), listOf()))?.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.HandshakeSuccess(expectedResult)), testEventDeliver.events)
    }

    @Test
    fun `failing receiveMessages push ReceiveFailure event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventDeliver = TestEventDeliver()
        val expectedReason = PubNubException("test")
        val factory = createSubscribeManagedEffectFactory(
            eventDeliver = testEventDeliver, receiveMessagesProvider = failingReceiveMessagesProvider(expectedReason)
        )

        // when
        factory.create(SubscribeEffectInvocation.ReceiveMessages(listOf(), listOf(), SubscriptionCursor(42L, "42")))
            ?.runEffect { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.ReceiveFailure(expectedReason)), testEventDeliver.events)
    }

    @Test
    fun `succeeding receiveMessages push ReceiveSuccess event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventDeliver = TestEventDeliver()
        val expectedResult = ReceiveMessagesResult(
            messages = listOf(), subscriptionCursor = SubscriptionCursor(42L, "42")
        )
        val factory = createSubscribeManagedEffectFactory(
            eventDeliver = testEventDeliver, receiveMessagesProvider = successfulReceiveMessageProvider(expectedResult)
        )

        // when
        factory.create(SubscribeEffectInvocation.ReceiveMessages(listOf(), listOf(), SubscriptionCursor(42L, "42")))
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
            testEventDeliver.events
        )
    }

    @Test
    fun `should pass RECEIVE_RECONNECT_SUCCESS event when ReceiveReconnect effect finishes successfully`() {
        // given
        val latch = CountDownLatch(1)
        val testEventDeliver = TestEventDeliver()
        val expectedResult = ReceiveMessagesResult(
            messages = listOf(), subscriptionCursor = SubscriptionCursor(42L, "42")
        )
        val subscribeManagedEffectFactory = createSubscribeManagedEffectFactory(
            eventDeliver = testEventDeliver,
            receiveMessagesProvider = successfulReceiveMessageProvider(expectedResult)
        )
        val receiveReconnectInvocation = SubscribeEffectInvocation.ReceiveReconnect(
            listOf(),
            listOf(),
            SubscriptionCursor(42L, "42"),
            1,
            PubNubException("Unknown error")
        )

        // when
        subscribeManagedEffectFactory.create(receiveReconnectInvocation)?.runEffect { latch.countDown() }

        // then
        Thread.sleep(50)
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(
            listOf(
                Event.ReceiveReconnectSuccess(
                    expectedResult.messages,
                    expectedResult.subscriptionCursor
                )
            ),
            testEventDeliver.events
        )
    }

    @Test
    fun `should pass RECEIVE_RECONNECT_FAILURE event when receiveReconnect effect finishes with failure`() {
        // given
        val latch = CountDownLatch(1)
        val testEventDeliver = TestEventDeliver()
        val expectedReason = PubNubException("test")
        val subscribeManagedEffectFactory = createSubscribeManagedEffectFactory(
            eventDeliver = testEventDeliver,
            receiveMessagesProvider = failingReceiveMessagesProvider(expectedReason)
        )
        val receiveReconnectInvocation = SubscribeEffectInvocation.ReceiveReconnect(
            listOf(),
            listOf(),
            SubscriptionCursor(42L, "42"),
            1,
            PubNubException("Unknown error")
        )

        // when
        subscribeManagedEffectFactory.create(receiveReconnectInvocation)?.runEffect { latch.countDown() }

        // then
        Thread.sleep(50)
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.ReceiveReconnectFailure(expectedReason)), testEventDeliver.events)
    }

    @Test
    fun `should pass HANDSHAKE_RECONNECT_SUCCESS event when handshakeReconnect effect finishes successfully`() {
        // given
        val latch = CountDownLatch(1)
        val testEventDeliver = TestEventDeliver()
        val expectedResult = SubscriptionCursor(1337L, "1337")
        val channels = listOf("channel1")
        val channelGroups = listOf("channelGroup1")
        val subscribeManagedEffectFactory = createSubscribeManagedEffectFactory(
            eventDeliver = testEventDeliver,
            handshakeProvider = successfulHandshakeProvider(expectedResult)
        )
        val handshakeReconnectInvocation = SubscribeEffectInvocation.HandshakeReconnect(
            channels,
            channelGroups,
            1,
            PubNubException("Unknown error")
        )

        // when
        subscribeManagedEffectFactory.create(handshakeReconnectInvocation)?.runEffect { latch.countDown() }

        // then
        Thread.sleep(50)
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(
            listOf(Event.HandshakeReconnectSuccess(channels, channelGroups, expectedResult)),
            testEventDeliver.events
        )
    }

    @Test
    fun `should pass HANDSHAKE_RECONNECT_FAILURE event when handshakeReconnect effect finishes with failure`() {
        // given
        val latch = CountDownLatch(1)
        val testEventDeliver = TestEventDeliver()
        val expectedReason = PubNubException("test")
        val channels = listOf("channel1")
        val channelGroups = listOf("channelGroup1")
        val subscribeManagedEffectFactory = createSubscribeManagedEffectFactory(
            eventDeliver = testEventDeliver,
            handshakeProvider = failingHandshakeProvider(expectedReason)
        )
        val handshakeReconnectInvocation = SubscribeEffectInvocation.HandshakeReconnect(
            channels,
            channelGroups,
            1,
            PubNubException("Unknown error")
        )

        // when
        subscribeManagedEffectFactory.create(handshakeReconnectInvocation)?.runEffect { latch.countDown() }

        // then
        Thread.sleep(50)
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.HandshakeReconnectFailure(expectedReason)), testEventDeliver.events)
    }

    private fun failingReceiveMessagesProvider(exception: PubNubException = PubNubException("Unknown error")) =
        ReceiveMessagesProvider { _, _, _ ->
            failingRemoteAction(exception)
        }

    private fun successfulReceiveMessageProvider(value: ReceiveMessagesResult) =
        ReceiveMessagesProvider { _, _, _ ->
            successfulRemoteAction(value)
        }

    private fun failingHandshakeProvider(exception: PubNubException = PubNubException("Unknown error")): HandshakeProvider =
        HandshakeProvider { _, _ -> failingRemoteAction(exception) }

    private fun successfulHandshakeProvider(value: SubscriptionCursor): HandshakeProvider =
        HandshakeProvider { _, _ -> successfulRemoteAction(value) }

    private fun createSubscribeManagedEffectFactory(
        eventDeliver: EventDeliver,
        handshakeProvider: HandshakeProvider = failingHandshakeProvider(exception = PubNubException("Unknown error")),
        receiveMessagesProvider: ReceiveMessagesProvider = failingReceiveMessagesProvider(exception = PubNubException("Unknown error"))
    ): SubscribeManagedEffectFactory = SubscribeManagedEffectFactory(
        eventDeliver = eventDeliver,
        handshakeProvider = handshakeProvider,
        receiveMessagesProvider = receiveMessagesProvider,
        policy = TestPolicy()
    )

    class TestEventDeliver : EventDeliver {
        val events = mutableListOf<Event>()

        @Synchronized
        override fun passEventForHandling(event: Event) {
            events.add(event)
        }
    }

    class TestPolicy : RetryPolicy() {
        override val maxRetries: Int = 100

        override fun computeDelay(count: Int): Duration {
            return Duration.ofMillis(10)
        }
    }
}
