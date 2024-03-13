package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import com.pubnub.internal.v2.callbacks.EventListenerCore
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubscriptionImplTest {
    private lateinit var objectUnderTest: TestableSubscriptionImpl

    private val pubnub: PubNubImpl = mockk(relaxed = true)
    private val pubnubCore: PubNubCore = mockk()
    private val channels: Set<ChannelName> = emptySet()
    private val channelGroups: Set<ChannelGroupName> = emptySet()
    private val options: SubscriptionOptions = EmptyOptions
    private val eventEmitter: EventEmitterImpl = mockk(relaxed = true)
    private val listener: EventListenerCore = mockk()

    @BeforeEach
    internal fun setUp() {
        every { pubnub.pubNubCore } returns pubnubCore
        objectUnderTest = TestableSubscriptionImpl(pubnub, channels, channelGroups, options) { eventEmitter }
    }

    @Test
    fun `addListener should call addListener on eventEmitter`() {
        objectUnderTest.testAddListener(listener)

        verify(exactly = 1) { eventEmitter.addListener(listener) }
    }

    @Test
    fun `removeListener should call removeListener on eventEmitter`() {
        objectUnderTest.testRemoveListener(listener)

        verify(exactly = 1) { eventEmitter.removeListener(listener) }
    }

    @Test
    fun `removeAllListeners should call removeAllListeners on eventEmitter`() {
        objectUnderTest.testRemoveAllListeners()

        verify(exactly = 1) { eventEmitter.removeAllListeners() }
    }

    // A temporary concrete implementation of BaseSubscriptionImpl for testing
    private class TestableSubscriptionImpl(
        pubnub: PubNubImpl,
        channels: Set<ChannelName>,
        channelGroups: Set<ChannelGroupName>,
        options: SubscriptionOptions,
        eventEmitterFactory: (BaseSubscriptionImpl<EventListener>) -> EventEmitterImpl,
    ) : BaseSubscriptionImpl<EventListener>(pubnub.pubNubCore, channels, channelGroups, options, eventEmitterFactory) {
        fun testAddListener(listener: EventListenerCore) = addListener(listener)

        fun testRemoveListener(listener: Listener) = removeListener(listener)

        fun testRemoveAllListeners() = removeAllListeners()

        override fun addListener(listener: EventListener) {
            doNothing()
        }

        private fun doNothing() {}
    }
}
