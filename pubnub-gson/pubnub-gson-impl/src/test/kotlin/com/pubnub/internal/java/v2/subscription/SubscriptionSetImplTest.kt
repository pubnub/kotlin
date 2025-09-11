package com.pubnub.internal.java.v2.subscription

import com.pubnub.api.logging.LogConfig
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.managers.ListenerManager
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubscriptionSetImplTest {
    private lateinit var objectUnderTest: SubscriptionSetImpl

    private val pubNubImpl: PubNubForJavaImpl = mockk()

    private val channel = setOf(ChannelName("Channel2"))
    private val channelGroup = setOf(ChannelGroupName("ChannelGroup2"))

    @BeforeEach
    fun setUp() {
        val testLogConfig = LogConfig("test-instance-id", "test-user-id")
        every { pubNubImpl.logConfig } returns testLogConfig
        val listenerManager = ListenerManager(pubNubImpl)
        every { pubNubImpl.listenerManager } returns listenerManager

        objectUnderTest = SubscriptionSetImpl(pubNubImpl, emptySet())
    }

    @Test
    fun `should add subscription to subscription set when using plusAssign method`() {
        // given
        val subscriptionToBeAdded = SubscriptionImpl(pubNubImpl, channel, channelGroup, SubscriptionOptions.receivePresenceEvents())

        // when
        objectUnderTest += subscriptionToBeAdded

        // then
        assertEquals(1, objectUnderTest.subscriptions.size)
        assertTrue(objectUnderTest.subscriptions.contains(subscriptionToBeAdded))
    }

    @Test
    fun `should remove subscription from subscription set when using minusAssign method`() {
        // given
        val subscriptionToBeRemoved = SubscriptionImpl(pubNubImpl, channel, channelGroup, EmptyOptions)
        objectUnderTest += subscriptionToBeRemoved
        assertTrue(objectUnderTest.subscriptions.contains(subscriptionToBeRemoved))

        // when
        objectUnderTest -= subscriptionToBeRemoved

        // then
        assertEquals(0, objectUnderTest.subscriptions.size)
    }
}

//
// import com.pubnub.api.PubNub
// import com.pubnub.api.java.v2.callbacks.handlers.OnChannelMetadataHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnFileHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnMembershipHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnMessageActionHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnMessageHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnPresenceHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnSignalHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnUuidMetadataHandler
// import com.pubnub.api.v2.subscriptions.SubscriptionSet
// import io.mockk.mockk
// import io.mockk.verify
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
//
// class SubscriptionSetImplTest {
//    private lateinit var objectUnderTest: SubscriptionSetImpl
//    private val underlyingSet: SubscriptionSet = mockk()
//    private val pubNub: PubNub = mockk(relaxed = true)
//
//    @BeforeEach
//    fun setUp() {
//        objectUnderTest = SubscriptionSetImpl(underlyingSet)
//    }
//
//    @Test
//    fun `setOnMessage sets onMessageHandler in EmitterHelper`() {
//        val onMessageHandler: OnMessageHandler = mockk()
//
//        objectUnderTest.setOnMessage(onMessageHandler)
//
//        verify { underlyingSet.onMessage = any() }
//    }
//
//    @Test
//    fun `setOnSignal should set property on underlyingSet`() {
//        val onSignalHandler: OnSignalHandler = mockk()
//
//        objectUnderTest.setOnSignal(onSignalHandler)
//
//        verify { underlyingSet.onSignal = any() }
//    }
//
//    @Test
//    fun `setOnPresence should set property on underlyingSet`() {
//        val onPresenceHandler: OnPresenceHandler = mockk()
//
//        objectUnderTest.setOnPresence(onPresenceHandler)
//
//        verify { underlyingSet.onPresence = any() }
//    }
//
//    @Test
//    fun `setOnMessageAction should set property on underlyingSet`() {
//        val onMessageActionHandler: OnMessageActionHandler = mockk()
//
//        objectUnderTest.setOnMessageAction(onMessageActionHandler)
//
//        verify { underlyingSet.onMessageAction = any() }
//    }
//
//    @Test
//    fun `setOnUuidMetadata should set property on underlyingSet`() {
//        val onUuidMetadataHandler: OnUuidMetadataHandler = mockk()
//
//        objectUnderTest.setOnUuidMetadata(onUuidMetadataHandler)
//
//        verify { underlyingSet.onObjects = any() }
//    }
//
//    @Test
//    fun `setOnChannelMetadata should set property on underlyingSet`() {
//        val onChannelMetadataHandler: OnChannelMetadataHandler = mockk()
//
//        objectUnderTest.setOnChannelMetadata(onChannelMetadataHandler)
//
//        verify { underlyingSet.onObjects = any() }
//    }
//
//    @Test
//    fun `setOnMembership should set property on underlyingSet`() {
//        val onMembershipHandler: OnMembershipHandler = mockk()
//
//        objectUnderTest.setOnMembership(onMembershipHandler)
//
//        verify { underlyingSet.onObjects = any() }
//    }
//
//    @Test
//    fun `setOnFile should set property on underlyingSet`() {
//        val onFileHandler: OnFileHandler = mockk()
//
//        objectUnderTest.setOnFile(onFileHandler)
//
//        verify { underlyingSet.onFile = any() }
//    }
// }
