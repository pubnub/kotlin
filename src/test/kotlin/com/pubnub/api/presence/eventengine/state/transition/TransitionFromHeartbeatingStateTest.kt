package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.transition
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatingStateTest {
    val channels = setOf("Channel01", "Channel02")
    val channelGroups = setOf("ChannelGroup01", "ChannelGroup02")
    val reason = PubNubException("Test")

    @Test
    fun `should transit from HEARTBEATING to INACTIVE and create LEAVE invocations when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.Heartbeating(channels, channelGroups),
            PresenceEvent.LeftAll
        )

        // then
        Assertions.assertEquals(PresenceState.HearbeatInactive, newState)
        Assertions.assertEquals(
            setOf(PresenceEffectInvocation.Leave(channels, channelGroups)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEAT_STOPPED and create LEAVE invocations when there is DISCONNECT event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.Heartbeating(channels, channelGroups),
            PresenceEvent.Disconnect
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatStopped(channels, channelGroups), newState)
        Assertions.assertEquals(
            setOf(PresenceEffectInvocation.Leave(channels, channelGroups)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEATING and create HEARTBEAT invocations when there is JOINED event`() {
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.Heartbeating(channels, channelGroups),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(channels + newChannels, channelGroups + newChannelGroup), newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.Heartbeat(channels + newChannels, channelGroups + newChannelGroup)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEATING and create LEAVE and HEARTBEAT invocations when there is LEFT event`() {
        // given
        val channelToLeave = setOf("Channel01")
        val channelGroupToLeave = setOf("ChannelGroup01")

        // when
        val (newState, invocations) = transition(
            PresenceState.Heartbeating(channels, channelGroups),
            PresenceEvent.Left(channelToLeave, channelGroupToLeave)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(channels - channelToLeave, channelGroups - channelGroupToLeave), newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.Leave(channelToLeave, channelGroupToLeave),
                PresenceEffectInvocation.Heartbeat(channels - channelToLeave, channelGroups - channelGroupToLeave)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEATING and create HEARTBEAT invocations when there is SET_STATE event`() {
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.Heartbeating(channels, channelGroups),
            PresenceEvent.StateSet(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            setOf(PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)), invocations
        )
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEAT_COOLDOWN and create SCHEDULE_NEXT_HEARTBEAT invocations when there is HEARTBEAT_SUCCESS event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.Heartbeating(channels, channelGroups),
            PresenceEvent.HeartbeatSuccess
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatCooldown(channels, channelGroups), newState)
        Assertions.assertTrue(invocations.any { it is PresenceEffectInvocation.ScheduleNextHeartbeat })
        Assertions.assertEquals(1, invocations.size)
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEAT_RECONNECTING and create DELEYED_HEARTBEAT invocations when there is HEARTBEAT_FAILURE event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.Heartbeating(channels, channelGroups),
            PresenceEvent.HeartbeatFailure(reason)
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatReconnecting(channels, channelGroups, 0, reason), newState)
        Assertions.assertTrue(invocations.any { it is PresenceEffectInvocation.DelayedHeartbeat })
        Assertions.assertEquals(1, invocations.size)
    }
}
