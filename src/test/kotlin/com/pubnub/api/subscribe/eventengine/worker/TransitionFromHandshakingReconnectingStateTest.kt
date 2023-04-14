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

class TransitionFromHandshakingReconnectingStateTest {

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_RECONNECTING_when_there_is_HANDSHAKE_RECONNECT_FAILURE_event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        // when
        val (handshakeReconnecting, effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeReconnecting) = transition(
            State.HandshakeReconnecting(channels, channelGroups),
            Event.HandshakeReconnectFailure(channels, channelGroups)
        )

        // then
        Assert.assertEquals(State.HandshakeReconnecting(channels, channelGroups), handshakeReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeReconnecting,
            Matchers.contains(
                EffectInvocation.CancelHandshakeReconnect,
                EffectInvocation.HandshakeReconnect(channels, channelGroups),
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_RECONNECTING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        // when
        val (handshakeReconnecting, effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeReconnecting) = transition(
            State.HandshakeReconnecting(channels, channelGroups),
            Event.SubscriptionChanged(channels, channelGroups, subscriptionCursor)
        )

        // then
        Assert.assertEquals(State.HandshakeReconnecting(channels, channelGroups), handshakeReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeReconnecting,
            Matchers.contains(
                EffectInvocation.CancelHandshakeReconnect,
                EffectInvocation.HandshakeReconnect(channels, channelGroups),
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_STOPPED_when_there_is_DISCONNECT_event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        // when
        val (handshakeStoppedState, effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeStopped) = transition(
            State.HandshakeReconnecting(channels, channelGroups),
            Event.Disconnect()
        )

        // then
        Assert.assertEquals(State.HandshakeStopped, handshakeStoppedState)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeStopped,
            Matchers.contains(
                EffectInvocation.CancelHandshakeReconnect,
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_FAILED_when_there_is_HANDSHAKE_RECONNECT_GIVEUP_event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        // when
        val (handshakeFailed, effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeFailed) = transition(
            State.HandshakeReconnecting(channels, channelGroups),
            Event.HandshakeReconnectGiveUp()
        )

        // then
        Assert.assertEquals(State.HandshakeFailed, handshakeFailed)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeFailed,
            Matchers.contains(
                EffectInvocation.CancelHandshakeReconnect,
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_RECEIVING_when_there_is_HANDSHAKE_RECONNECT_SUCCESS_event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        // when
        val (receiving, effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeFailed) = transition(
            State.HandshakeReconnecting(channels, channelGroups),
            Event.HandshakeReconnectSuccess(channels, channelGroups, subscriptionCursor)
        )

        // then
        Assert.assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeFailed,
            Matchers.contains(
                EffectInvocation.CancelHandshakeReconnect,
                EffectInvocation.EmitStatus("Connected"),
                EffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        // when
        val (receiving, effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeFailed) = transition(
            State.HandshakeReconnecting(channels, channelGroups),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        Assert.assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromHandshakeReconnectingToHandshakeFailed,
            Matchers.contains(
                EffectInvocation.CancelHandshakeReconnect,
                EffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            )
        )
    }
}
