package com.pubnub.api.presence.eventengine.state.transition

import com.pubnub.api.eventengine.transition
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.presence.eventengine.state.PresenceState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHearbeatInactiveStateTest {
    val channels = setOf("Channel01")
    val channelGroups = setOf("ChannelGroup01")

    @Test
    fun `should transit from INACTIVE to HEARTBEATING and creat HEARTBEAT invocation when there is JOINED event`() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (newState, invocations) = transition(PresenceState.HearbeatInactive, PresenceEvent.Joined(newChannels, newChannelGroup))

        // then
        assertEquals(PresenceState.Heartbeating(newChannels, newChannelGroup), newState)
        assertEquals(setOf<PresenceEffectInvocation>(PresenceEffectInvocation.Heartbeat(newChannels, newChannelGroup)), invocations)
    }

    @Test
    fun `should not make transition from INACTIVE and create no invocation when there is LEFT event`() {
        // when
        val (newState, invocations) = transition(PresenceState.HearbeatInactive, PresenceEvent.Left(channels, channelGroups))

        // then
        assertEquals(PresenceState.HearbeatInactive, newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should not make transition from INACTIVE when there is RECONNECT event and create no invocation`() {
        // when
        val (newState, invocations) = transition(PresenceState.HearbeatInactive, PresenceEvent.Reconnect)

        // then
        assertEquals(PresenceState.HearbeatInactive, newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }

    @Test
    fun `should not make transition from INACTIVE when there is SET_STATE event`() {
        // when
        val (newState, invocations) = transition(PresenceState.HearbeatInactive, PresenceEvent.StateSet(channels, channelGroups))

        // then
        assertEquals(PresenceState.HearbeatInactive, newState)
        assertEquals(emptySet<PresenceEffectInvocation>(), invocations)
    }
}
