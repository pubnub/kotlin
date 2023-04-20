package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromReceiveFailedStateTest {
    @Test
    fun can_transit_from_RECEIVE_FAILED_to_RECEIVE_RECONNECTING_when_there_is_RECEIVE_RECONNECT_RETRY_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)
        val reason = PubNubException("Test")

        // when
        val (state, invocations) = transition(
            State.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason), Event.ReceiveReconnectRetry
        )

        // then
        assertEquals(
            State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason), state
        )
        assertEquals(
            listOf(
                SubscribeEffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 0, reason)
            ),
            invocations
        )
    }
}
