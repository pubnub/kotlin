package com.pubnub.internal.subscribe.eventengine.event

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SubscribeEventTest {
    private val timeToken = 12345345452L
    private val region = "42"
    private val subscriptionCursor = SubscriptionCursor(timeToken, region)

    @Test
    fun `channel and channelGroup in SubscriptionChanged event should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val subscriptionChanged: SubscribeEvent.SubscriptionChanged =
            SubscribeEvent.SubscriptionChanged(myMutableSetOfChannels, myMutableSetOfChannelGroups)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(subscriptionChanged.channels.contains(channelName))
        assertTrue(subscriptionChanged.channelGroups.contains(channelGroupName))
    }

    @Test
    fun `channel and channelGroup in SubscriptionRestored event should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val subscriptionRestored: SubscribeEvent.SubscriptionRestored =
            SubscribeEvent.SubscriptionRestored(myMutableSetOfChannels, myMutableSetOfChannelGroups, subscriptionCursor)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(subscriptionRestored.channels.contains(channelName))
        assertTrue(subscriptionRestored.channelGroups.contains(channelGroupName))
    }
}
