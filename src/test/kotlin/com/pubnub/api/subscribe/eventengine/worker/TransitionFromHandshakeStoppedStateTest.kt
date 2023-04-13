package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.jupiter.api.Test

class TransitionFromHandshakeStoppedStateTest {

    @Test
    fun can_transit_from_HANDSHAKE_STOPPED_to_HANDSHAKE_RECONNECTING_when_there_is_RECONNECT_Event() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        //when
        val (handshakeReconnecting, effectInvocationsForTransitionFromHandshakeFailedToHandshakeReconnecting) = transition(
            State.HandshakeStopped,
            Event.Reconnect(channels, channelGroups)
        )

        //then
        Assert.assertEquals(State.HandshakeReconnecting(channels, channelGroups), handshakeReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeFailedToHandshakeReconnecting,
            Matchers.contains(
                EffectInvocation.HandshakeReconnect(channels, channelGroups),
            )
        )
    }

}
