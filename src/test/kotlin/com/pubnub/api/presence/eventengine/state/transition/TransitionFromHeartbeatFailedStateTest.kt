package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.transition
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatFailedStateTest {
    val channels = setOf("Channel01", "Channel02")
    val channelGroups = setOf("ChannelGroup01", "ChannelGroup02")
    val reason = PubNubException("Test")

    @Test
    fun `channel and channelGroup should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val heartbeatFailed: PresenceState.HeartbeatFailed = PresenceState.HeartbeatFailed(myMutableSetOfChannels, myMutableSetOfChannelGroups, reason)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        Assertions.assertTrue(heartbeatFailed.channels.contains(channelName))
        Assertions.assertTrue(heartbeatFailed.channelGroups.contains(channelGroupName))
    }

    @Test
    fun `should transit from HEARTBEAT_FAILED to INACTIVE and create LEAVE invocation when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.LeftAll
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatInactive, newState)
        Assertions.assertEquals(
            setOf<PresenceEffectInvocation>(PresenceEffectInvocation.Leave(channels, channelGroups)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_FAILED to INACTIVE and create LEAVE invocation when there is LEFT event and no channels remain`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.Left(channels, channelGroups)
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatInactive, newState)
        Assertions.assertEquals(
            setOf<PresenceEffectInvocation>(PresenceEffectInvocation.Leave(channels, channelGroups)), invocations
        )
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
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(newChannels, heartbeating.channels)
        Assertions.assertEquals(newChannelGroup, heartbeating.channelGroups)
        Assertions.assertEquals(
            setOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)), invocations
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
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(channels, heartbeating.channels)
        Assertions.assertEquals(channelGroups, heartbeating.channelGroups)
        Assertions.assertEquals(
            setOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(channels, channelGroups)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_FAILED to HEARTBEATING and creat HEARTBEAT and LEAVE invocation when there is LEFT event`() {
        // given
        val channelToLeave = setOf("Channel01")
        val channelGroupToLeave = setOf("ChannelGroup01")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatFailed(channels, channelGroups, reason),
            PresenceEvent.Left(channelToLeave, channelGroupToLeave)
        )

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(channels - channelToLeave, heartbeating.channels)
        Assertions.assertEquals(channelGroups - channelGroupToLeave, heartbeating.channelGroups)
        Assertions.assertEquals(
            setOf(PresenceEffectInvocation.Leave(channelToLeave, channelGroupToLeave), PresenceEffectInvocation.Heartbeat(channels - channelToLeave, channelGroups - channelGroupToLeave)), invocations
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
        Assertions.assertTrue(newState is PresenceState.HeartbeatStopped)
        val heartbeatStopped = newState as PresenceState.HeartbeatStopped
        Assertions.assertEquals(channels, heartbeatStopped.channels)
        Assertions.assertEquals(channelGroups, heartbeatStopped.channelGroups)
        Assertions.assertEquals(
            setOf(PresenceEffectInvocation.Leave(channels, channelGroups)), invocations
        )
    }
}
