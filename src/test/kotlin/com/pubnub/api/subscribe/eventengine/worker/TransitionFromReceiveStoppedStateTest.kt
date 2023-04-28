package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromReceiveStoppedStateTest {
    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_RECEIVE_RECONNECTING_when_there_is_RECONNECT_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            Event.Reconnect
        )

        // then
        assertEquals(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, null),
            state
        )
        assertEquals(
            listOf(
                SubscribeEffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 0, null)
            ),
            invocations
        )
    }
}
