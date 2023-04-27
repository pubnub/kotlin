package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHandshakeStoppedStateTest {

    @Test
    fun can_transit_from_HANDSHAKE_STOPPED_to_HANDSHAKE_RECONNECTING_when_there_is_RECONNECT_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeStopped(channels, channelGroups, null),
            Event.Reconnect
        )

        // then
        assertEquals(SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, null), state)
        assertEquals(listOf(SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, 0, null)), invocations)
    }
}
