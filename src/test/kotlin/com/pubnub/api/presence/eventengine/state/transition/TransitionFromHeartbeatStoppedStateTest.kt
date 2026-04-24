package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.eventengine.transition
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatStoppedStateTest {
    val channels = setOf("Channel01", "Channel02")
    val channelGroups = setOf("ChannelGroup01", "ChannelGroup02")

    @Test
    fun `should transit from HEARTBEAT_STOPPED to INACTIVE when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.LeftAll)

        // then
        assertEquals(PresenceState.HeartbeatInactive, newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEAT_STOPPED when there is JOIN event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.Joined(newChannels, newChannelGroup))

        // then
        assertEquals(PresenceState.HeartbeatStopped(newChannels, newChannelGroup), newState)
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
        assertEquals(PresenceState.HeartbeatStopped(channels - channelToLeave, channelGroups - channelGroupToLeave), newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEAT_STOPPED invocation when there is SET_STATE event`() {
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.StateSet(newChannels, newChannelGroup))

        // then
        assertEquals(PresenceState.HeartbeatStopped(newChannels, newChannelGroup), newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEATING and creat HEARTBEAT invocation when there is RECONNECT event`() {
        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.Reconnect)

        // then
        assertEquals(PresenceState.Heartbeating(channels, channelGroups), newState)
        assertEquals(setOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(channels, channelGroups)), invocations)
    }
}
