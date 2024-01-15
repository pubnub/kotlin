package com.pubnub.internal.presence.eventengine.state.transition

import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHeartbeatInactiveStateTest {
    val channels = setOf("Channel01")
    val channelGroups = setOf("ChannelGroup01")

    @Test
    fun `should transit from INACTIVE to HEARTBEATING and creat HEARTBEAT invocation when there is JOINED event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatInactive, PresenceEvent.Joined(newChannels, newChannelGroup))

        // then
        Assertions.assertTrue(newState is PresenceState.Heartbeating)
        val heartbeating = newState as PresenceState.Heartbeating
        Assertions.assertEquals(newChannels, heartbeating.channels)
        Assertions.assertEquals(newChannelGroup, heartbeating.channelGroups)
        assertEquals(setOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)), invocations)
    }

    @Test
    fun `should not make transition from INACTIVE and create no invocation when there is LEFT event`() {
        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatInactive, PresenceEvent.Left(channels, channelGroups))

        // then
        assertEquals(PresenceState.HeartbeatInactive, newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should not make transition from INACTIVE when there is RECONNECT event and create no invocation`() {
        // when
        val (newState, invocations) = transition(PresenceState.HeartbeatInactive, PresenceEvent.Reconnect)

        // then
        assertEquals(PresenceState.HeartbeatInactive, newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }
}
