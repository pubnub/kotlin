package com.pubnub.internal.presence.eventengine.state.transition

import com.pubnub.api.PubNubException
import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatReconnectingStateTest {
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
        val heartbeatReconnecting: PresenceState.HeartbeatReconnecting =
            PresenceState.HeartbeatReconnecting(myMutableSetOfChannels, myMutableSetOfChannelGroups, 0, reason)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(heartbeatReconnecting.channels.contains(channelName))
        assertTrue(heartbeatReconnecting.channelGroups.contains(channelGroupName))
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to INACTIVE and create LEAVE and CANCEL_DELEYED_HEARTBEAT invocations when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.HeartbeatReconnecting(
                    channels,
                    channelGroups,
                    0,
                    reason,
                ),
                PresenceEvent.LeftAll,
            )

        // then
        assertEquals(PresenceState.HeartbeatInactive, newState)
        assertEquals(
            setOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups),
            ),
            invocations,
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to INACTIVE and create LEAVE and CANCEL_DELEYED_HEARTBEAT invocations when there is LEFT event and no channels remain`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.HeartbeatReconnecting(
                    channels,
                    channelGroups,
                    0,
                    reason,
                ),
                PresenceEvent.Left(channels, channelGroups),
            )

        // then
        assertTrue(newState is PresenceState.HeartbeatInactive)
        assertEquals(
            setOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups),
            ),
            invocations,
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEATING and create CANCEL_DELEYED_HEARTBEAT, LEAVE and HEARTBEAT invocations when there is LEFT event`() {
        // given
        val channelToLeave = setOf("Channel01")
        val channelGroupToLeave = setOf("ChannelGroup01")

        // when
        val (newState, invocations) =
            transition(
                PresenceState.HeartbeatReconnecting(
                    channels,
                    channelGroups,
                    0,
                    reason,
                ),
                PresenceEvent.Left(channelToLeave, channelGroupToLeave),
            )

        // then
        assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        assertEquals(channels - channelToLeave, heartbeating.channels)
        assertEquals(channelGroups - channelGroupToLeave, heartbeating.channelGroups)
        assertEquals(
            setOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Leave(channelToLeave, channelGroupToLeave),
                PresenceEffectInvocation.Heartbeat(channels - channelToLeave, channelGroups - channelGroupToLeave),
            ),
            invocations,
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEAT_STOPPED and create CANCEL_DELEYED_HEARTBEAT and LEAVE invocations when there is DISCONNECT event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.HeartbeatReconnecting(
                    channels,
                    channelGroups,
                    0,
                    reason,
                ),
                PresenceEvent.Disconnect,
            )

        // then
        assertTrue(newState is PresenceState.HeartbeatStopped)
        val heartbeatStopped = newState as PresenceState.HeartbeatStopped
        assertEquals(channels, heartbeatStopped.channels)
        assertEquals(channelGroups, heartbeatStopped.channelGroups)
        assertEquals(
            setOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups),
            ),
            invocations,
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEATING and create CANCEL_DELEYED_HEARTBEAT and HEARTBEAT invocations when there is JOINED event`() {
        // given
        val channelToJoin = setOf("NewChannel")
        val channelGroupToJoin = setOf("NewChannelGroup")

        // when
        val (newState, invocations) =
            transition(
                PresenceState.HeartbeatReconnecting(
                    channels,
                    channelGroups,
                    0,
                    reason,
                ),
                PresenceEvent.Joined(channelToJoin, channelGroupToJoin),
            )

        // then
        assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        assertEquals(channels + channelToJoin, heartbeating.channels)
        assertEquals(channelGroups + channelGroupToJoin, heartbeating.channelGroups)

        assertEquals(
            setOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
                PresenceEffectInvocation.Heartbeat(channels + channelToJoin, channelGroups + channelGroupToJoin),
            ),
            invocations,
        )
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEAT_COOLDOWN and create CANCEL_DELEYED_HEARTBEAT and WAIT invocations when there is HEARTBEAT_SUCCESS event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.HeartbeatReconnecting(
                    channels,
                    channelGroups,
                    0,
                    reason,
                ),
                PresenceEvent.HeartbeatSuccess,
            )

        // then
        assertTrue(newState is PresenceState.HeartbeatCooldown)
        val heartbeatCooldown = newState as PresenceState.HeartbeatCooldown
        assertEquals(channels, heartbeatCooldown.channels)
        assertEquals(channelGroups, heartbeatCooldown.channelGroups)
        assertTrue(invocations.any { it is PresenceEffectInvocation.Wait })
        assertTrue(invocations.any { it is PresenceEffectInvocation.CancelDelayedHeartbeat })
        assertEquals(2, invocations.size)
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEAT_RECONNECTING and create CANCEL_DELEYED_HEARTBEAT and DELEYED_HEARTBEAT invocations when there is HEARTBEAT_FAILURE event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.HeartbeatReconnecting(
                    channels,
                    channelGroups,
                    0,
                    reason,
                ),
                PresenceEvent.HeartbeatFailure(reason),
            )

        // then
        assertTrue(newState is PresenceState.HeartbeatReconnecting)
        val heartbeatReconnecting = newState as PresenceState.HeartbeatReconnecting
        assertEquals(channels, heartbeatReconnecting.channels)
        assertEquals(channelGroups, heartbeatReconnecting.channelGroups)
        assertEquals(1, heartbeatReconnecting.attempts)
        assertEquals(reason, heartbeatReconnecting.reason)
        assertTrue(invocations.any { it is PresenceEffectInvocation.DelayedHeartbeat })
        assertTrue(invocations.any { it is PresenceEffectInvocation.CancelDelayedHeartbeat })
        assertEquals(2, invocations.size)
    }

    @Test
    fun `should transit from HEARTBEAT_RECONNECTING to HEARTBEAT_FAILED and create CANCEL_DELEYED_HEARTBEAT invocations when there is HEARTBEAT_GIVUP event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.HeartbeatReconnecting(
                    channels,
                    channelGroups,
                    0,
                    reason,
                ),
                PresenceEvent.HeartbeatGiveup(reason),
            )

        // then
        assertTrue(newState is PresenceState.HeartbeatFailed)
        val heartbeatFailed = newState as PresenceState.HeartbeatFailed
        assertEquals(channels, heartbeatFailed.channels)
        assertEquals(channelGroups, heartbeatFailed.channelGroups)
        assertEquals(reason, heartbeatFailed.reason)
        assertEquals(
            setOf(
                PresenceEffectInvocation.CancelDelayedHeartbeat,
            ),
            invocations,
        )
    }
}
