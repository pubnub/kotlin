package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import com.pubnub.api.presence.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatStoppedStateTest {
    val channels = setOf("Channel01", "Channel02")
    val channelGroups = setOf("ChannelGroup01", "ChannelGroup02")

    @Test
    fun `should transit from HEARTBEAT_STOPPED to INACTIVE when there is LEFT_ALL event`(){
        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.LeftAll)

        // then
        assertEquals(PresenceState.HearbeatInactive, newState)
        assertEquals(emptyList<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEAT_STOPPED when there is JOIN event`(){
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.Joined(newChannels, newChannelGroup))

        // then
        assertEquals(PresenceState.HeartbeatStopped(newChannels, newChannelGroup), newState)
        assertEquals(emptyList<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEAT_STOPPED when there is LEFT event`(){
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.Left(newChannels, newChannelGroup))

        // then
        assertEquals(PresenceState.HeartbeatStopped(newChannels, newChannelGroup), newState)
        assertEquals(emptyList<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEATING and creat HEARTBEAT invocation when there is SET_STATE event`(){
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.StateSet(newChannels, newChannelGroup))

        // then
        assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        assertEquals(listOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_STOPPED to HEARTBEATING and creat HEARTBEAT invocation when there is RECONNECT event`(){
        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatStopped(channels, channelGroups), PresenceEvent.Reconnect)

        // then
        assertEquals(PresenceState.Heartbeating(channels, channelGroups), newState)
        assertEquals(listOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(channels, channelGroups)), invocations)
    }
}
