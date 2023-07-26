package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.transition
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatWaitingStateTest {
    val channels = setOf("Channel01", "Channel02")
    val channelGroups = setOf("ChannelGroup01", "ChannelGroup02")
    val reason = PubNubException("Test")

    @Test
    fun `should transit from HEARTBEAT_WAITING to INACTIVE and create CANCEL_SCHEDULE_NEXT_HEARTBEAT and LEAVE invocations when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatWaiting(channels, channelGroups),
            PresenceEvent.LeftAll
        )

        // then
        Assertions.assertEquals(PresenceState.HearbeatInactive, newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelScheduleNextHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_WAITING to HEARTBEATING and create CANCEL_SCHEDULE_NEXT_HEARTBEAT, LEAVE and HEARTBEAT invocations when there is LEFT event`() {
        // given
        val channelToLeave = setOf("Channel01")
        val channelGroupToLeave = setOf("ChannelGroup01")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatWaiting(channels, channelGroups),
            PresenceEvent.Left(channelToLeave, channelGroupToLeave)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(channels - channelToLeave, channelGroups - channelGroupToLeave), newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelScheduleNextHeartbeat,
                PresenceEffectInvocation.Leave(channelToLeave, channelGroupToLeave),
                PresenceEffectInvocation.Heartbeat(channels - channelToLeave, channelGroups - channelGroupToLeave)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_WAITING to HEARTBEAT_STOPPED and create CANCEL_SCHEDULE_NEXT_HEARTBEAT and LEAVE invocations when there is DISCONNECT event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatWaiting(channels, channelGroups),
            PresenceEvent.Disconnect
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatStopped(channels, channelGroups), newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelScheduleNextHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_WAITING to HEARTBEATING and create CANCEL_SCHEDULE_NEXT_HEARTBEAT and HEARTBEAT invocations when there is JOINED event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatWaiting(channels, channelGroups),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelScheduleNextHeartbeat,
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_WAITING to HEARTBEATING and create CANCEL_SCHEDULE_NEXT_HEARTBEAT and HEARTBEAT invocations when there is STATE_SET event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatWaiting(channels, channelGroups),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelScheduleNextHeartbeat,
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_WAITING to HEARTBEATING and create CANCEL_SCHEDULE_NEXT_HEARTBEAT and HEARTBEAT invocations when there is NEXT_HEARTBEAT event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatWaiting(channels, channelGroups),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelScheduleNextHeartbeat,
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }
}
