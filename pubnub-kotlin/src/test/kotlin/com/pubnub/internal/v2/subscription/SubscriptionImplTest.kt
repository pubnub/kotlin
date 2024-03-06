package com.pubnub.internal.v2.subscription

import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubscriptionImplTest {
    private lateinit var objectUnderTest: SubscriptionImpl

    private val pubNubImpl: PubNubImpl = mockk(relaxed = true)
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
        val subscriptionToBeAdded = SubscriptionImpl(pubNubImpl, channels02, channelGroups02, SubscriptionOptions.receivePresenceEvents())
        every { pubNubImpl.subscriptionSetOf(any<Set<Subscription>>()) } returns subscriptionSetImpl
        every { subscriptionSetImpl.subscriptions } returns setOf(objectUnderTest, subscriptionToBeAdded)

        // when
        val subscriptionSet: SubscriptionSet = objectUnderTest + subscriptionToBeAdded

        // then
        assertEquals(2, subscriptionSet.subscriptions.size)
        assertTrue(subscriptionSet.subscriptions.contains(objectUnderTest))
        assertTrue(subscriptionSet.subscriptions.contains(subscriptionToBeAdded))
        verify { pubNubImpl.subscriptionSetOf(setOf(objectUnderTest, subscriptionToBeAdded)) }
    }
}
