package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.jupiter.api.Test

class TransitionFromReceiveStoppedStateTest {
    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_RECEIVE_RECONNECTING_when_there_is_RECONNECT_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        // when
        val (receiveReconnecting, effectInvocationsForTransitionFromReceiveStoppedToReceiveReconnecting) = transition(
            State.ReceiveStopped(channels, channelGroups),
            Event.Reconnect(channels, channelGroups)
        )

        // then
        Assert.assertEquals(State.ReceiveReconnecting(channels, channelGroups), receiveReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceiveStoppedToReceiveReconnecting,
            Matchers.contains(
                EffectInvocation.ReceiveReconnect(channels, channelGroups)
            )
        )
    }
}
