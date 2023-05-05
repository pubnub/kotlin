package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.EventDeliver
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SubscribeManagedEffectFactoryTest {

    private val handshakeProvider = mockk<HandshakeProvider>()
    private val receiveMessageProvider = mockk<ReceiveMessagesProvider>()
    private val eventDeliver = mockk<EventDeliver>()
    private val policy = mockk<RetryPolicy>()
    private val executorService = mockk<ScheduledExecutorService>()
    private val channels = listOf("channel1")
    private val channelGroups = listOf("channelGroup1")
    private lateinit var subscribeManagedEffectFactory: SubscribeManagedEffectFactory
    private val attempts = 1
    private val reason = PubNubException("Unknown error")
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")

    @BeforeEach
    fun setUp() {
        subscribeManagedEffectFactory = SubscribeManagedEffectFactory(
            handshakeProvider,
            receiveMessageProvider,
            eventDeliver,
            policy,
            executorService
        )
    }

    @Test
    fun `should return handshake effect when getting Handshake invocation`() {
        // when
        val managedEffect =
            subscribeManagedEffectFactory.create(SubscribeEffectInvocation.Handshake(channels, channelGroups))

        // then
        assertThat(managedEffect, instanceOf(HandshakeEffect::class.java))
    }

    @Test
    fun `should return handshakeReconnect effect when getting HandshakeReconnect invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(
            SubscribeEffectInvocation.HandshakeReconnect(
                channels,
                channelGroups,
                attempts,
                reason
            )
        )

        // then
        assertThat(managedEffect, instanceOf(HandshakeReconnectEffect::class.java))
    }

    @Test
    fun `should return receiveMessages effect when getting ReceiveMessages invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(
            SubscribeEffectInvocation.ReceiveMessages(
                channels,
                channelGroups,
                subscriptionCursor
            )
        )

        // then
        assertThat(managedEffect, instanceOf(ReceiveMessagesEffect::class.java))
    }

    @Test
    fun `should return receiveReconnect effect when getting ReceiveReconnect invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(
            SubscribeEffectInvocation.ReceiveReconnect(
                channels,
                channelGroups,
                subscriptionCursor,
                attempts,
                reason
            )
        )

        // then
        assertThat(managedEffect, instanceOf(ReceiveReconnectEffect::class.java))
    }

    @Test
    fun `should return null when getting CancelHandshake invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(SubscribeEffectInvocation.CancelHandshake)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelHandshakeReconnect invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(SubscribeEffectInvocation.CancelHandshakeReconnect)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelReceiveMessages invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(SubscribeEffectInvocation.CancelReceiveMessages)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelReceiveReconnect invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(SubscribeEffectInvocation.CancelReceiveReconnect)

        // then
        assertNull(managedEffect)
    }

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
