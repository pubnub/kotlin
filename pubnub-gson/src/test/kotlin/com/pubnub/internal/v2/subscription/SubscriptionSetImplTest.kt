package com.pubnub.internal.v2.subscription

import com.pubnub.api.v2.callbacks.handlers.OnChannelMetadataHandler
import com.pubnub.api.v2.callbacks.handlers.OnFileHandler
import com.pubnub.api.v2.callbacks.handlers.OnMembershipHandler
import com.pubnub.api.v2.callbacks.handlers.OnMessageActionHandler
import com.pubnub.api.v2.callbacks.handlers.OnMessageHandler
import com.pubnub.api.v2.callbacks.handlers.OnPresenceHandler
import com.pubnub.api.v2.callbacks.handlers.OnSignalHandler
import com.pubnub.api.v2.callbacks.handlers.OnUuidMetadataHandler
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.managers.ListenerManager
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubscriptionSetImplTest {
    private lateinit var objectUnderTest: SubscriptionSetImpl

    private val pubNubCore: PubNubCore = mockk(relaxed = true)
    private val emitterHelper: EmitterHelper = mockk(relaxed = true)
    private val listenerManager: ListenerManager = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        every { pubNubCore.listenerManager } returns listenerManager
        every { emitterHelper.initialize(any()) } just Runs
        objectUnderTest = SubscriptionSetImpl(pubNubCore, emptySet(), emitterHelper)
    }

    @Test
    fun `setOnMessage sets onMessageHandler in EmitterHelper`() {
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
