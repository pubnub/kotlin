package com.pubnub.internal.v2.subscription

import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
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

    private val pubNubImpl: PubNubImpl = mockk()

    private val channel = setOf(ChannelName("Channel2"))
    private val channelGroup = setOf(ChannelGroupName("ChannelGroup2"))

    @BeforeEach
    fun setUp() {
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
