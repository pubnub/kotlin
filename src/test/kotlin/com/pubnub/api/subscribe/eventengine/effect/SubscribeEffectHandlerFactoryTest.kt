package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SubscribeEffectHandlerFactoryTest {

    @Test
    fun `failing handshake runs the onCompletion block`() {
        // given
        val latch = CountDownLatch(1)
        val factory = SubscribeEffectHandlerFactory(
            eventHandler = TestEventHandler(), handshakeProvider = failingHandshakeProvider()
        )

        // when
        factory.create(EffectInvocation.Handshake(listOf(), listOf())).run { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
    }

    @Test
    fun `failing handshake push HandshakeFailure event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventHandler = TestEventHandler()
        val expectedReason = PubNubException("test")
        val factory = SubscribeEffectHandlerFactory(
            eventHandler = testEventHandler,
            handshakeProvider = failingHandshakeProvider(expectedReason)
        )

        // when
        factory.create(EffectInvocation.Handshake(listOf(), listOf())).run { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.HandshakeFailure(expectedReason)), testEventHandler.events)
    }

    @Test
    fun `succeeding handshake runs the onCompletion block`() {
        // given
        val latch = CountDownLatch(1)
        val factory = SubscribeEffectHandlerFactory(
            eventHandler = TestEventHandler(), handshakeProvider = successfullHandshakeProvider()
        )

        // when
        factory.create(EffectInvocation.Handshake(listOf(), listOf())).run { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
    }

    @Test
    fun `succeeding handshake push HandshakeSuccess event`() {
        // given
        val latch = CountDownLatch(1)
        val testEventHandler = TestEventHandler()
        val expectedResult = SubscriptionCursor(1337L, "1337")
        val factory = SubscribeEffectHandlerFactory(
            eventHandler = testEventHandler,
            handshakeProvider = successfullHandshakeProvider(expectedResult)
        )

        // when
        factory.create(EffectInvocation.Handshake(listOf(), listOf())).run { latch.countDown() }

        // then
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS))
        assertEquals(listOf(Event.HandshakeSuccess(expectedResult)), testEventHandler.events)
    }

    fun failingHandshakeProvider(exception: PubNubException = PubNubException("Unknown error")) = object : HandshakeProvider {
        override fun handshake(channels: List<String>, channelGroups: List<String>) =
            failingRemoteAction<SubscriptionCursor>(exception)
    }

    fun successfullHandshakeProvider(value: SubscriptionCursor = SubscriptionCursor(42L, "42")) = object : HandshakeProvider {
        override fun handshake(channels: List<String>, channelGroups: List<String>) =
            successfullRemoteAction(value)
    }

    fun <T> failingRemoteAction(exception: PubNubException): RemoteAction<T> = object : RemoteAction<T> {
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

    fun <T> successfullRemoteAction(result: T): RemoteAction<T> = object : RemoteAction<T> {
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

    class TestEventHandler : EventHandler {
        val events = mutableListOf<Event>()

        @Synchronized
        override fun handleEvent(event: Event) {
            events.add(event)
        }
    }
}
