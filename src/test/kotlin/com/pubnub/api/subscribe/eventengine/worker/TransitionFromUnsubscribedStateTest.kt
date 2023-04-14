package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransitionFromUnsubscribedStateTest {
    @Test
    fun can_transit_from_UNSUBSRIBED_to_HANDSHAKING_when_there_is_subscriptionChangeEvent() {
        // given
        val currentState = State.Unsubscribed
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        val subscriptionChange = Event.SubscriptionChanged(channels, channelGroups, subscriptionCursor)

        // when
        val (handshaking, effectInvocationsForTransitionFromUnsubscribedToHandshaking) = transition(
            currentState,
            subscriptionChange
        )

        // then
        Assertions.assertEquals(State.Handshaking(channels, channelGroups), handshaking)
        assertThat(
            effectInvocationsForTransitionFromUnsubscribedToHandshaking,
            Matchers.contains(
                EffectInvocation.Handshake(channels, channelGroups)
            )
        )
    }

    @Test
    fun can_transit_from_UNSUBSRIBED_to_RECEIVING_when_there_is_subscriptionRestoredEvent() {
        // given
        val currentState = State.Unsubscribed
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)
        val subscriptionRestoredEvent = Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)

        // when
        val (receiving, effectInvocationsForTransitionFromUnsubscribedToReceiving) = transition(
            currentState,
            subscriptionRestoredEvent
        )

        // then
        assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        assertThat(
            effectInvocationsForTransitionFromUnsubscribedToReceiving,
            Matchers.contains(
                EffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            )
        )
    }
}
