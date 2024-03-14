package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.handlers.OnChannelMetadataHandler
import com.pubnub.api.v2.callbacks.handlers.OnFileHandler
import com.pubnub.api.v2.callbacks.handlers.OnMembershipHandler
import com.pubnub.api.v2.callbacks.handlers.OnMessageActionHandler
import com.pubnub.api.v2.callbacks.handlers.OnMessageHandler
import com.pubnub.api.v2.callbacks.handlers.OnPresenceHandler
import com.pubnub.api.v2.callbacks.handlers.OnSignalHandler
import com.pubnub.api.v2.callbacks.handlers.OnUuidMetadataHandler
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
    private val emitterHelper: EmitterHelper = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        every { pubnub.pubNubCore } returns pubnubCore
        every { eventEmitter.addListener(any()) } returns Unit
        every { emitterHelper.initialize(eventEmitter) } returns Unit
        objectUnderTest = SubscriptionImpl(pubnub, channels, channelGroups, options, emitterHelper) { eventEmitter }
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

    @Test
    fun `setOnMessage should set property on emitterHelper`() {
        val onMessageHandler: OnMessageHandler = mockk()

        objectUnderTest.setOnMessage(onMessageHandler)

        verify { emitterHelper.onMessage = onMessageHandler }
    }

    @Test
    fun `setOnSignal should set property on emitterHelper`() {
        val onSignalHandler: OnSignalHandler = mockk()

        objectUnderTest.setOnSignal(onSignalHandler)

        verify { emitterHelper.onSignal = onSignalHandler }
    }

    @Test
    fun `setOnPresence should set property on emitterHelper`() {
        val onPresenceHandler: OnPresenceHandler = mockk()

        objectUnderTest.setOnPresence(onPresenceHandler)

        verify { emitterHelper.onPresence = onPresenceHandler }
    }

    @Test
    fun `setOnMessageAction should set property on emitterHelper`() {
        val onMessageActionHandler: OnMessageActionHandler = mockk()

        objectUnderTest.setOnMessageAction(onMessageActionHandler)

        verify { emitterHelper.onMessageAction = onMessageActionHandler }
    }

    @Test
    fun `setOnUuidMetadata should set property on emitterHelper`() {
        val onUuidMetadataHandler: OnUuidMetadataHandler = mockk()

        objectUnderTest.setOnUuidMetadata(onUuidMetadataHandler)

        verify { emitterHelper.onUuid = onUuidMetadataHandler }
    }

    @Test
    fun `setOnChannelMetadata should set property on emitterHelper`() {
        val onChannelMetadataHandler: OnChannelMetadataHandler = mockk()

        objectUnderTest.setOnChannelMetadata(onChannelMetadataHandler)

        verify { emitterHelper.onChannel = onChannelMetadataHandler }
    }

    @Test
    fun `setOnMembership should set property on emitterHelper`() {
        val onMembershipHandler: OnMembershipHandler = mockk()

        objectUnderTest.setOnMembership(onMembershipHandler)

        verify { emitterHelper.onMembership = onMembershipHandler }
    }

    @Test
    fun `setOnFile should set property on emitterHelper`() {
        val onFileHandler: OnFileHandler = mockk()

        objectUnderTest.setOnFile(onFileHandler)

        verify { emitterHelper.onFile = onFileHandler }
    }
}
