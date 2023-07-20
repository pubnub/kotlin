package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.PubNubException
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import com.pubnub.api.presence.eventengine.transition.transition
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatFailedStateTest {
    val channels = setOf("Channel01", "Channel02")
    val channelGroups = setOf("ChannelGroup01", "ChannelGroup02")
    val reason = PubNubException("Test")

    @Test
    fun `should transit from HEARTBEAT_FAILED to INACTIVE when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.LeftAll
        )

        // then
        Assertions.assertEquals(PresenceState.HearbeatInactive, newState)
        Assertions.assertEquals(emptyList<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should transit from HEARTBEAT_FAILED to HEARTBEATING and creat HEARTBEAT invocation when there is JOINED event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            listOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_FAILED to HEARTBEATING and creat HEARTBEAT invocation when there is SET_STATE event`() {
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.StateSet(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            listOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_FAILED to HEARTBEATING and creat HEARTBEAT invocation when there is RECONNECT event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.Reconnect
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(channels, channelGroups), newState)
        Assertions.assertEquals(
            listOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(channels, channelGroups)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_FAILED to HEARTBEATING and creat HEARTBEAT invocation when there is LEFT event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.Left(channels, channelGroups)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(channels, channelGroups), newState)
        Assertions.assertEquals(
            listOf(PresenceEffectInvocation.Leave(channels, channelGroups), PresenceEffectInvocation.Heartbeat(channels, channelGroups)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_FAILED to HEARTBEAT_STOPED when there is DISCONNECT event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.Disconnect
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatStopped(channels, channelGroups), newState)
        Assertions.assertEquals(emptyList<PresenceEffectInvocation>(), invocations)
    }
}
