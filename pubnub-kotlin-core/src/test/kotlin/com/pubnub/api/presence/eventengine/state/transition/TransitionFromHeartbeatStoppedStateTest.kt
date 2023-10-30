package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.eventengine.transition
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatStoppedStateTest {
    val channels = setOf("Channel01", "Channel02")
    val channelGroups = setOf("ChannelGroup01", "ChannelGroup02")

    @Test
    fun `channel and channelGroup should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val heartbeatStopped: PresenceState.HeartbeatStopped = PresenceState.HeartbeatStopped(myMutableSetOfChannels, myMutableSetOfChannelGroups)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(heartbeatStopped.channels.contains(channelName))
        assertTrue(heartbeatStopped.channelGroups.contains(channelGroupName))
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to INACTIVE when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.LeftAll)

        // then
        assertEquals(PresenceState.HeartbeatInactive, newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to INACTIVE when there is LEFT event and no channels remain`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatStopped(channels, channelGroups),
            PresenceEvent.Left(channels, channelGroups)
        )

        // then
        assertTrue(newState is PresenceState.HeartbeatInactive)
        assertTrue(invocations.isEmpty())
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEAT_STOPPED when there is JOIN event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.Joined(newChannels, newChannelGroup))

        // then
        assertTrue(newState is PresenceState.HeartbeatStopped)
        val heartbeatStopped = newState as PresenceState.HeartbeatStopped
        assertEquals(newChannels, heartbeatStopped.channels)
        assertEquals(newChannelGroup, heartbeatStopped.channelGroups)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEAT_STOPPED when there is LEFT event`() {
        // given
        val channelToLeave = setOf("Channel01")
        val channelGroupToLeave = setOf("ChannelGroup01")

        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.Left(channelToLeave, channelGroupToLeave))

        // then
        assertTrue(newState is PresenceState.HeartbeatStopped)
        val heartbeatStopped = newState as PresenceState.HeartbeatStopped
        assertEquals(channels - channelToLeave, heartbeatStopped.channels)
        assertEquals(channelGroups - channelGroupToLeave, heartbeatStopped.channelGroups)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEATING and creat HEARTBEAT invocation when there is RECONNECT event`() {
        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.Reconnect)

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(channels, heartbeating.channels)
        Assertions.assertEquals(channelGroups, heartbeating.channelGroups)
        assertEquals(setOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(channels, channelGroups)), invocations)
    }
}
