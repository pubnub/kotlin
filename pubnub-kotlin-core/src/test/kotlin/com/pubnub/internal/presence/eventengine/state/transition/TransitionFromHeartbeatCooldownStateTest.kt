package com.pubnub.internal.presence.eventengine.state.transition

import com.pubnub.api.PubNubException
import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatCooldownStateTest {
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
        val heartbeatCooldown: PresenceState.HeartbeatCooldown = PresenceState.HeartbeatCooldown(myMutableSetOfChannels, myMutableSetOfChannelGroups)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        Assertions.assertTrue(heartbeatCooldown.channels.contains(channelName))
        Assertions.assertTrue(heartbeatCooldown.channelGroups.contains(channelGroupName))
    }

    @Test
    fun `should transit from HEARTBEAT_COOLDOWN to INACTIVE and create CANCEL_WAIT and LEAVE invocations when there is LEFT_ALL event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatCooldown(channels, channelGroups),
            PresenceEvent.LeftAll
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatInactive, newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelWait,
                PresenceEffectInvocation.Leave(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_COOLDOWN to INACTIVE and create CANCEL_WAIT and LEAVE invocations when there is LEFT event and no channels remain`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatCooldown(channels, channelGroups),
            PresenceEvent.Left(channels, channelGroups)
        )

        // then
        Assertions.assertEquals(PresenceState.HeartbeatInactive, newState)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelWait,
                PresenceEffectInvocation.Leave(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_COOLDOWN to HEARTBEATING and create CANCEL_WAIT, LEAVE and HEARTBEAT invocations when there is LEFT event`() {
        // given
        val channelToLeave = setOf("Channel01")
        val channelGroupToLeave = setOf("ChannelGroup01")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatCooldown(channels, channelGroups),
            PresenceEvent.Left(channelToLeave, channelGroupToLeave)
        )

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(channels - channelToLeave, heartbeating.channels)
        Assertions.assertEquals(channelGroups - channelGroupToLeave, heartbeating.channelGroups)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelWait,
                PresenceEffectInvocation.Leave(channelToLeave, channelGroupToLeave),
                PresenceEffectInvocation.Heartbeat(channels - channelToLeave, channelGroups - channelGroupToLeave)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_COOLDOWN to HEARTBEAT_STOPPED and create CANCEL_WAIT and LEAVE invocations when there is DISCONNECT event`() {
        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatCooldown(channels, channelGroups),
            PresenceEvent.Disconnect
        )

        // then
        Assertions.assertTrue(newState is PresenceState.HeartbeatStopped)
        val heartbeatStopped = newState as PresenceState.HeartbeatStopped
        Assertions.assertEquals(channels, heartbeatStopped.channels)
        Assertions.assertEquals(channelGroups, heartbeatStopped.channelGroups)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelWait,
                PresenceEffectInvocation.Leave(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_COOLDOWN to HEARTBEATING and create CANCEL_WAIT and HEARTBEAT invocations when there is JOINED event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatCooldown(channels, channelGroups),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(newChannels, heartbeating.channels)
        Assertions.assertEquals(newChannelGroup, heartbeating.channelGroups)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelWait,
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_COOLDOWN to HEARTBEATING and create CANCEL_WAIT and HEARTBEAT invocations when there is STATE_SET event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatCooldown(channels, channelGroups),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(newChannels, heartbeating.channels)
        Assertions.assertEquals(newChannelGroup, heartbeating.channelGroups)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelWait,
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }

    @Test
    fun `should transit from HEARTBEAT_COOLDOWN to HEARTBEATING and create CANCEL_WAIT and HEARTBEAT invocations when there is TIMES_UP event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(
            PresenceState.HeartbeatCooldown(channels, channelGroups),
            PresenceEvent.Joined(newChannels, newChannelGroup)
        )

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(newChannels, heartbeating.channels)
        Assertions.assertEquals(newChannelGroup, heartbeating.channelGroups)
        Assertions.assertEquals(
            setOf(
                PresenceEffectInvocation.CancelWait,
                PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)
            ),
            invocations
        )
    }
}
