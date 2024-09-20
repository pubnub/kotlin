
import com.pubnub.api.java.v2.subscriptions.Subscription
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.subscription.SubscriptionImpl
import com.pubnub.internal.java.v2.subscription.SubscriptionSetImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// package com.pubnub.internal.java.v2.subscription
//
// import com.pubnub.api.java.callbacks.SubscribeCallback
// import com.pubnub.api.java.v2.callbacks.EventListener
// import com.pubnub.api.java.v2.callbacks.handlers.OnChannelMetadataHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnFileHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnMembershipHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnMessageActionHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnMessageHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnPresenceHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnSignalHandler
// import com.pubnub.api.java.v2.callbacks.handlers.OnUuidMetadataHandler
// import com.pubnub.api.v2.subscriptions.Subscription
// import io.mockk.mockk
// import io.mockk.slot
// import io.mockk.verify
// import org.junit.jupiter.api.Assertions.assertEquals
// import org.junit.jupiter.api.Assertions.assertTrue
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
//
// class SubscriptionImplTest {
//    private lateinit var objectUnderTest: SubscriptionImpl
//    val underlyingSubscription: Subscription = mockk()
//    private val eventListener: EventListener = mockk()
//    private val listenerSubscribeCallback: SubscribeCallback = mockk()
//
//    @BeforeEach
//    fun setUp() {
//        objectUnderTest = SubscriptionImpl(underlyingSubscription)
//    }
//
//    @Test
//    fun `addListener should call addListener on eventEmitter`() {
//        val capturedListeners = mutableListOf<EventListener>()
//        objectUnderTest.addListener(eventListener)
//
//        verify { underlyingSubscription.addListener(capture(capturedListeners)) }
//        assertTrue(capturedListeners.contains(eventListener))
//    }
//
//    @Test
//    fun `removeListener which is SubscribeCallback should call proper removeListener on eventEmitter`() {
//        val delegatingSubscribeCallbackSlot = slot<SubscribeCallback>()
//        objectUnderTest.removeListener(listenerSubscribeCallback)
//
//        verify(exactly = 1) { underlyingSubscription.removeListener(capture(delegatingSubscribeCallbackSlot)) }
//        assertEquals(delegatingSubscribeCallbackSlot.captured, listenerSubscribeCallback)
//    }
//
//    @Test
//    fun `removeListener which is EventListener should call proper removeListener on eventEmitter`() {
//        val delegatingEventListenerSlot = slot<EventListener>()
//        objectUnderTest.removeListener(eventListener)
//
//        verify(exactly = 1) { underlyingSubscription.removeListener(capture(delegatingEventListenerSlot)) }
//        assertEquals(delegatingEventListenerSlot.captured, eventListener)
//    }
//
//    @Test
//    fun `removeAllListeners should call removeAllListeners on eventEmitter`() {
//        objectUnderTest.removeAllListeners()
//
//        verify(exactly = 1) { underlyingSubscription.removeAllListeners() }
//    }
//
//    @Test
//    fun `setOnMessage should set property on emitterHelper`() {
//        val onMessageHandler: OnMessageHandler = mockk()
//
//        objectUnderTest.setOnMessage(onMessageHandler)
//
//        verify { underlyingSubscription.onMessage = any() }
//    }
//
//    @Test
//    fun `setOnSignal should set property on emitterHelper`() {
//        val onSignalHandler: OnSignalHandler = mockk()
//
//        objectUnderTest.setOnSignal(onSignalHandler)
//
//        verify { underlyingSubscription.onSignal = any() }
//    }
//
//    @Test
//    fun `setOnPresence should set property on emitterHelper`() {
//        val onPresenceHandler: OnPresenceHandler = mockk()
//
//        objectUnderTest.setOnPresence(onPresenceHandler)
//
//        verify { underlyingSubscription.onPresence = any() }
//    }
//
//    @Test
//    fun `setOnMessageAction should set property on emitterHelper`() {
//        val onMessageActionHandler: OnMessageActionHandler = mockk()
//
//        objectUnderTest.setOnMessageAction(onMessageActionHandler)
//
//        verify { underlyingSubscription.onMessageAction = any() }
//    }
//
//    @Test
//    fun `setOnUuidMetadata should set property on emitterHelper`() {
//        val onUuidMetadataHandler: OnUuidMetadataHandler = mockk()
//
//        objectUnderTest.setOnUuidMetadata(onUuidMetadataHandler)
//
//        verify { underlyingSubscription.onObjects = any() }
//    }
//
//    @Test
//    fun `setOnChannelMetadata should set property on emitterHelper`() {
//        val onChannelMetadataHandler: OnChannelMetadataHandler = mockk()
//
//        objectUnderTest.setOnChannelMetadata(onChannelMetadataHandler)
//
//        verify { underlyingSubscription.onObjects = any() }
//    }
//
//    @Test
//    fun `setOnMembership should set property on emitterHelper`() {
//        val onMembershipHandler: OnMembershipHandler = mockk()
//
//        objectUnderTest.setOnMembership(onMembershipHandler)
//
//        verify { underlyingSubscription.onObjects = any() }
//    }
//
//    @Test
//    fun `setOnFile should set property on emitterHelper`() {
//        val onFileHandler: OnFileHandler = mockk()
//
//        objectUnderTest.setOnFile(onFileHandler)
//
//        verify { underlyingSubscription.onFile = any() }
//    }
// }

class SubscriptionImplTest {
    private lateinit var objectUnderTest: Subscription

    private val pubNubImpl: PubNubForJavaImpl = mockk(relaxed = true)
    private val subscriptionSetImpl: SubscriptionSetImpl = mockk()

    private val channels = setOf(ChannelName("Channel1"))
    private val channels02 = setOf(ChannelName("Channel2"))
    private val channelGroups = setOf(ChannelGroupName("ChannelGroup1"))
    private val channelGroups02 = setOf(ChannelGroupName("ChannelGroup2"))

    @BeforeEach
    fun setUp() {
        objectUnderTest = SubscriptionImpl(pubNubImpl, channels, channelGroups, SubscriptionOptions.receivePresenceEvents())
    }

    @Test
    fun `should add subscription to subscription creating subscriptionSet when using plus method`() {
        // given
        val subscriptionToBeAdded: Subscription =
            SubscriptionImpl(pubNubImpl, channels02, channelGroups02, SubscriptionOptions.receivePresenceEvents())
        every { pubNubImpl.subscriptionSetOf(any<Set<Subscription>>()) } returns subscriptionSetImpl
        every { subscriptionSetImpl.subscriptions } returns setOf(objectUnderTest, subscriptionToBeAdded)

        // when
        val subscriptionSet: SubscriptionSet = objectUnderTest + subscriptionToBeAdded

        // then
        assertEquals(2, subscriptionSet.subscriptions.size)
        assertTrue(subscriptionSet.subscriptions.contains(objectUnderTest))
        assertTrue(subscriptionSet.subscriptions.contains(subscriptionToBeAdded))
    }
}
