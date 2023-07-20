package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.PubNubException
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import com.pubnub.api.presence.eventengine.transition.transition
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatReconnectingStateTest {
    val channels = setOf("Channel01", "Channel02")
    val channelGroups = setOf("ChannelGroup01", "ChannelGroup02")
    val reason = PubNubException("Test")

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to INACTIVE and create LEAVE and CANCEL_DELEYED_HEARTBEAT invocations when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatReconnecting(
                channels,
                channelGroups,
                0,
                reason
            ),
            PresenceEvent.LeftAll
        )

        // then
        Assertions.assertEquals(PresenceState.HearbeatInactive, newState)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEATING and create CANCEL_DELEYED_HEARTBEAT, LEAVE and HEARTBEAT invocations when there is LEFT event`() {
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatReconnecting(
                channels,
                channelGroups,
                0,
                reason
            ),
            PresenceEvent.Left(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Leave(newChannels, newChannelGroup),
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEAT_STOPPED and create CANCEL_DELEYED_HEARTBEAT and LEAVE invocations when there is DISCONNECT event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatReconnecting(
                channels,
                channelGroups,
                0,
                reason
            ),
            PresenceEvent.Disconnect
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatStopped(channels, channelGroups), newState)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups),
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEATING and create CANCEL_DELEYED_HEARTBEAT and HEARTBEAT invocations when there is JOINED event`() {
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatReconnecting(
                channels,
                channelGroups,
                0,
                reason
            ),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEATING and create CANCEL_DELEYED_HEARTBEAT and HEARTBEAT invocations when there is STATE_SET event`() {
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatReconnecting(
                channels,
                channelGroups,
                0,
                reason
            ),
            PresenceEvent.StateSet(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEAT_WAITING and create CANCEL_DELEYED_HEARTBEAT and SCHEDULE_NEXT_HEARTBEAT invocations when there is HEARTBEAT_SUCCESS event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatReconnecting(
                channels,
                channelGroups,
                0,
                reason
            ),
            PresenceEvent.HeartbeatSuccess
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatWaiting(channels, channelGroups), newState)
        Assertions.assertTrue(invocations.any { it is PresenceEffectInvocation.ScheduleNextHeartbeat })
        Assertions.assertTrue(invocations.any { it is PresenceEffectInvocation.CancelDelayedHeartbeat })
        Assertions.assertEquals(2, invocations.size)
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEAT_RECONNECTING and create CANCEL_DELEYED_HEARTBEAT and DELEYED_HEARTBEAT invocations when there is HEARTBEAT_FAILURE event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatReconnecting(
                channels,
                channelGroups,
                0,
                reason
            ),
            PresenceEvent.HeartbeatFailure(reason)
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatReconnecting(channels, channelGroups, 1, reason), newState)
        Assertions.assertTrue(invocations.any { it is PresenceEffectInvocation.DelayedHeartbeat })
        Assertions.assertTrue(invocations.any { it is PresenceEffectInvocation.CancelDelayedHeartbeat })
        Assertions.assertEquals(2, invocations.size)
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEAT_FAILED and create CANCEL_DELEYED_HEARTBEAT invocations when there is HEARTBEAT_GIVUP event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatReconnecting(
                channels,
                channelGroups,
                0,
                reason
            ),
            PresenceEvent.HeartbeatGiveup(reason)
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatFailed(channels, channelGroups, reason), newState)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
            ),
            invocations
        )
    }
}
