package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.eventengine.transition
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromUnsubscribedStateTest {
    val channels = listOf("Channel1")
    val channelGroups = listOf("ChannelGroup1")
    val timeToken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timeToken, region)

    @Test
    fun can_transit_from_UNSUBSRIBED_to_HANDSHAKING_when_there_is_subscriptionChangeEvent() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Unsubscribed, SubscribeEvent.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertEquals(SubscribeState.Handshaking(channels, channelGroups), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.Handshake(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_UNSUBSRIBED_to_RECEIVING_when_there_is_subscriptionRestoredEvent() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Unsubscribed, SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            invocations
        )
    }
}
