package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.eventengine.transition
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TransitionFromUnsubscribedStateTest {
    val channels = setOf("Channel1")
    val channelGroups = setOf("ChannelGroup1")
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
        Assertions.assertTrue(state is SubscribeState.Handshaking)
        val handshaking = state as SubscribeState.Handshaking
        assertEquals(channels, handshaking.channels)
        assertEquals(channelGroups, handshaking.channelGroups)
        assertEquals(
            setOf(
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
        Assertions.assertTrue(state is SubscribeState.Handshaking)
        val handshaking = state as SubscribeState.Handshaking
        assertEquals(channels, handshaking.channels)
        assertEquals(channelGroups, handshaking.channelGroups)
        assertEquals(subscriptionCursor, handshaking.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.Handshake(channels, channelGroups),
            ),
            invocations
        )
    }
}
