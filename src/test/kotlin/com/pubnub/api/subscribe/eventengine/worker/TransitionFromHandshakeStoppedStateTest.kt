package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHandshakeStoppedStateTest {

    @Test
    fun can_transit_from_HANDSHAKE_STOPPED_to_HANDSHAKING_when_there_is_RECONNECT_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val reason = PubNubException("Test")

        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeStopped(channels, channelGroups, reason),
            Event.Reconnect
        )

        // then
        assertEquals(SubscribeState.Handshaking(channels, channelGroups), state)
        assertEquals(listOf(SubscribeEffectInvocation.Handshake(channels, channelGroups)), invocations)
    }
}
