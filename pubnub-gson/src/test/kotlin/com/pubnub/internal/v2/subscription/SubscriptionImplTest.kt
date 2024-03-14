package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.callbacks.DelegatingSubscribeCallback
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import com.pubnub.internal.v2.callbacks.EventListenerCore
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubscriptionImplTest {
    private lateinit var objectUnderTest: SubscriptionImpl

    private val pubnub: PubNubImpl = mockk(relaxed = true)
    private val pubnubCore: PubNubCore = mockk()
    private val channels: Set<ChannelName> = emptySet()
    private val channelGroups: Set<ChannelGroupName> = emptySet()
    private val options: SubscriptionOptions = EmptyOptions
    private val eventEmitter: EventEmitterImpl = mockk(relaxed = true)
    private val eventListener: EventListener = mockk()
    private val listenerSubscribeCallback: SubscribeCallback = mockk()

    @BeforeEach
    internal fun setUp() {
        every { pubnub.pubNubCore } returns pubnubCore
        objectUnderTest = SubscriptionImpl(pubnub, channels, channelGroups, options) { eventEmitter }
    }

    @Test
    fun `addListener should call addListener on eventEmitter`() {
        val capturedListeners = mutableListOf<EventListenerCore>()
        objectUnderTest.addListener(eventListener)

        verify { eventEmitter.addListener(capture(capturedListeners)) }
        assertTrue(capturedListeners.all { it is DelegatingEventListener })
    }

    @Test
    fun `removeListener which is SubscribeCallback should call proper removeListener on eventEmitter`() {
        val delegatingSubscribeCallbackSlot = slot<DelegatingSubscribeCallback>()
        objectUnderTest.removeListener(listenerSubscribeCallback)

        verify(exactly = 1) { eventEmitter.removeListener(capture(delegatingSubscribeCallbackSlot)) }
        assertTrue(delegatingSubscribeCallbackSlot.captured is DelegatingSubscribeCallback)
        assertEquals(listenerSubscribeCallback, delegatingSubscribeCallbackSlot.captured.listener)
    }

    @Test
    fun `removeListener which is EventListener should call proper removeListener on eventEmitter`() {
        val delegatingEventListenerSlot = slot<DelegatingEventListener>()
        objectUnderTest.removeListener(eventListener)

        verify(exactly = 1) { eventEmitter.removeListener(capture(delegatingEventListenerSlot)) }
        assertTrue(delegatingEventListenerSlot.captured is DelegatingEventListener)
        assertEquals(eventListener, delegatingEventListenerSlot.captured.listener)
    }

    @Test
    fun `removeAllListeners should call removeAllListeners on eventEmitter`() {
        objectUnderTest.removeAllListeners()

        verify(exactly = 1) { eventEmitter.removeAllListeners() }
    }
}
