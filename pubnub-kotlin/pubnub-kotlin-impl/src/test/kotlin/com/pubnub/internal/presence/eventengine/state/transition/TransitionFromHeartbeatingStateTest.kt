package com.pubnub.internal.presence.eventengine.state.transition

import com.pubnub.api.PubNubException
import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatingStateTest {
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
        val heartbeating: PresenceState.Heartbeating =
            PresenceState.Heartbeating(myMutableSetOfChannels, myMutableSetOfChannelGroups)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        Assertions.assertTrue(heartbeating.channels.contains(channelName))
        Assertions.assertTrue(heartbeating.channelGroups.contains(channelGroupName))
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEATING and create CANCEL_HEARTBEAT and HEARTBEAT invocations when there is RECONNECT event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.Heartbeating(channels, channelGroups),
                PresenceEvent.Reconnect,
            )

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(channels, heartbeating.channels)
        Assertions.assertEquals(channelGroups, heartbeating.channelGroups)
        // the cancel must be dispatched before the new heartbeat
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelHeartbeat,
                PresenceEffectInvocation.Heartbeat(channels, channelGroups),
            ),
            invocations.toList(),
        )
    }

    @Test
    fun `should transit from HEARTBEATING to INACTIVE and create CANCEL_HEARTBEAT and LEAVE invocations when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.Heartbeating(channels, channelGroups),
                PresenceEvent.LeftAll,
            )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatInactive, newState)
        // the heartbeat must be cancelled before the leave goes out, or it can re-join the channels
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups),
            ),
            invocations.toList(),
        )
    }

    @Test
    fun `should not transit from HEARTBEATING when there is TIMES_UP event`() {
        // when
        val heartbeating = PresenceState.Heartbeating(channels, channelGroups)
        val (newState, invocations) = transition(heartbeating, PresenceEvent.TimesUp)

        // then
        Assertions.assertSame(heartbeating, newState)
        Assertions.assertEquals(0, invocations.size)
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEAT_STOPPED and create CANCEL_HEARTBEAT and LEAVE invocations when there is DISCONNECT event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.Heartbeating(channels, channelGroups),
                PresenceEvent.Disconnect,
            )

        // then
        Assertions.assertTrue(newState is PresenceState.HeartbeatStopped)
        val heartbeatStopped = newState as PresenceState.HeartbeatStopped
        Assertions.assertEquals(channels, heartbeatStopped.channels)
        Assertions.assertEquals(channelGroups, heartbeatStopped.channelGroups)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups),
            ),
            invocations.toList(),
        )
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEATING and create CANCEL_HEARTBEAT and HEARTBEAT invocations when there is JOINED event`() {
        // given
        val newChannels = setOf("NewChannel")
        val newChannelGroup = setOf("NewChannelGroup")

        // when
        val (newState, invocations) =
            transition(
                PresenceState.Heartbeating(channels, channelGroups),
                PresenceEvent.Joined(newChannels, newChannelGroup),
            )

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(channels + newChannels, heartbeating.channels)
        Assertions.assertEquals(channelGroups + newChannelGroup, heartbeating.channelGroups)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelHeartbeat,
                PresenceEffectInvocation.Heartbeat(channels + newChannels, channelGroups + newChannelGroup),
            ),
            invocations.toList(),
        )
    }

    @Test
    fun `should transit from HEARTBEATING to INACTIVE and create CANCEL_HEARTBEAT and LEAVE invocations when there is LEFT event and no channels remain`() {
        // given
        val channelToLeave = channels
        val channelGroupToLeave = channelGroups

        // when
        val (newState, invocations) =
            transition(
                PresenceState.Heartbeating(channels, channelGroups),
                PresenceEvent.Left(channelToLeave, channelGroupToLeave),
            )

        // then
        Assertions.assertTrue(newState is PresenceState.HeartbeatInactive)
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelHeartbeat,
                PresenceEffectInvocation.Leave(channels, channelGroups),
            ),
            invocations.toList(),
        )
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEATING and create CANCEL_HEARTBEAT, LEAVE and HEARTBEAT invocations when there is LEFT event`() {
        // given
        val channelToLeave = setOf("Channel01")
        val channelGroupToLeave = setOf("ChannelGroup01")

        // when
        val (newState, invocations) =
            transition(
                PresenceState.Heartbeating(channels, channelGroups),
                PresenceEvent.Left(channelToLeave, channelGroupToLeave),
            )

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(channels - channelToLeave, heartbeating.channels)
        Assertions.assertEquals(channelGroups - channelGroupToLeave, heartbeating.channelGroups)
        // the old heartbeat still carries the left channels, so it must be cancelled before the leave
        Assertions.assertEquals(
            listOf(
                PresenceEffectInvocation.CancelHeartbeat,
                PresenceEffectInvocation.Leave(channelToLeave, channelGroupToLeave),
                PresenceEffectInvocation.Heartbeat(channels - channelToLeave, channelGroups - channelGroupToLeave),
            ),
            invocations.toList(),
        )
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEAT_COOLDOWN and create CANCEL_HEARTBEAT and WAIT invocations when there is HEARTBEAT_SUCCESS event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.Heartbeating(channels, channelGroups),
                PresenceEvent.HeartbeatSuccess,
            )

        // then
        Assertions.assertTrue(newState is PresenceState.HeartbeatCooldown)
        val heartbeatCooldown = newState as PresenceState.HeartbeatCooldown
        Assertions.assertEquals(channels, heartbeatCooldown.channels)
        Assertions.assertEquals(channelGroups, heartbeatCooldown.channelGroups)
        Assertions.assertEquals(2, invocations.size)
        Assertions.assertEquals(PresenceEffectInvocation.CancelHeartbeat, invocations.first())
        Assertions.assertTrue(invocations.last() is PresenceEffectInvocation.Wait)
    }

    @Test
    fun `should transit from HEARTBEATING to HEARTBEAT_FAILED and create CANCEL_HEARTBEAT invocation when there is HEARTBEAT_FAILURE event`() {
        // when
        val (newState, invocations) =
            transition(
                PresenceState.Heartbeating(channels, channelGroups),
                PresenceEvent.HeartbeatFailure(reason),
            )

        // then
        Assertions.assertTrue(newState is PresenceState.HeartbeatFailed)
        val heartbeatFailed = newState as PresenceState.HeartbeatFailed
        Assertions.assertEquals(channels, heartbeatFailed.channels)
        Assertions.assertEquals(channelGroups, heartbeatFailed.channelGroups)
        Assertions.assertEquals(reason, heartbeatFailed.reason)
        Assertions.assertEquals(listOf(PresenceEffectInvocation.CancelHeartbeat), invocations.toList())
    }
}
