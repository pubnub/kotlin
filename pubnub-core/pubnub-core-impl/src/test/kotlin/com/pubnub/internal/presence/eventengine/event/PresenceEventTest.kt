package com.pubnub.internal.presence.eventengine.event

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PresenceEventTest {
    @Test
    fun `channel and channelGroup in Joined event should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val joined: PresenceEvent.Joined = PresenceEvent.Joined(myMutableSetOfChannels, myMutableSetOfChannelGroups)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        Assertions.assertTrue(joined.channels.contains(channelName))
        Assertions.assertTrue(joined.channelGroups.contains(channelGroupName))
    }

    @Test
    fun `channel and channelGroup in Left event should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val left: PresenceEvent.Left = PresenceEvent.Left(myMutableSetOfChannels, myMutableSetOfChannelGroups)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        Assertions.assertTrue(left.channels.contains(channelName))
        Assertions.assertTrue(left.channelGroups.contains(channelGroupName))
    }
}
