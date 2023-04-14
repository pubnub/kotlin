package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.jupiter.api.Test

class TransitionFromHandshakeFailedStateTest {

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKE_RECONNECTING_when_there_is_HANDSHAKE_RECONNECT_RETRY_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        // when
        val (handshakeReconnecting, effectInvocationsForTransitionFromHandshakeFailedToHandshakeReconnecting) = transition(
            State.HandshakeFailed,
            Event.HandshakeReconnectRetry(channels, channelGroups)
        )

        // then
        Assert.assertEquals(State.HandshakeReconnecting(channels, channelGroups), handshakeReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeFailedToHandshakeReconnecting,
            Matchers.contains(
                EffectInvocation.HandshakeReconnect(channels, channelGroups),
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKE_RECONNECTING_when_there_is_SUBSCRIPTION_CHANGED_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        // when
        val (handshakeReconnecting, effectInvocationsForTransitionFromHandshakeFailedToHandshakeReconnecting) = transition(
            State.HandshakeFailed,
            Event.SubscriptionChanged(channels, channelGroups, subscriptionCursor)
        )

        // then
        Assert.assertEquals(State.HandshakeReconnecting(channels, channelGroups), handshakeReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeFailedToHandshakeReconnecting,
            Matchers.contains(
                EffectInvocation.HandshakeReconnect(channels, channelGroups),
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_RECEIVE_RECONNECTING_when_there_is_SUBSCRIPTION_RESTORED_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        // when
        val (receiveReconnecting, effectInvocationsForTransitionFromHandshakeFailedToReceivingReconnecting) = transition(
            State.HandshakeFailed,
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        Assert.assertEquals(State.ReceiveReconnecting(channels, channelGroups), receiveReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeFailedToReceivingReconnecting,
            Matchers.contains(
                EffectInvocation.ReceiveReconnect(channels, channelGroups)
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKE_RECONNECTING_when_there_is_RECONNECT_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        // when
        val (handshakeReconnecting, effectInvocationsForTransitionFromHandshakeFailedToHandshakeReconnecting) = transition(
            State.HandshakeFailed,
            Event.Reconnect(channels, channelGroups)
        )

        // then
        Assert.assertEquals(State.HandshakeReconnecting(channels, channelGroups), handshakeReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeFailedToHandshakeReconnecting,
            Matchers.contains(
                EffectInvocation.HandshakeReconnect(channels, channelGroups),
            )
        )
    }
}
