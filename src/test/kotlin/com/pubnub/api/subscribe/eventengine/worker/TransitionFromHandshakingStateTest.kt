package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation.CancelPendingHandshakeRequest
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation.EmitStatus
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation.HandshakeReconnect
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation.HandshakeRequest
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation.ReceiveMessagesRequest
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHandshakingStateTest {

    @Test
    fun can_transit_from_HANDSHAKING_to_RECEIVING_when_there_is_handshakeSuccessEvent() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        val currentState = State.Handshaking(channels, channelGroups)
        val handshakingSuccessEvent = Event.HandshakeSuccess(subscriptionCursor)

        //when
        val (receiving, effectInvocationsForTransitionFromHandshakingToReceiving) = transition(
            currentState,
            handshakingSuccessEvent
        )

        //then
        assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        assertThat(
            effectInvocationsForTransitionFromHandshakingToReceiving,
            Matchers.contains(
                CancelPendingHandshakeRequest,
                EmitStatus("Connected"),
                ReceiveMessagesRequest(channels, channelGroups, subscriptionCursor)
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_RECEIVING_when_there_is_subscriptionRestoredEvent() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        val currentState = State.Handshaking(channels, channelGroups)
        val subscriptionRestoredEvent = Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)

        //when
        val (receiving, effectInvocationsForTransitionFromHandshakingToReceiving) = transition(
            currentState,
            subscriptionRestoredEvent
        )

        //then
        assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        assertThat(
            effectInvocationsForTransitionFromHandshakingToReceiving,
            Matchers.contains(
                CancelPendingHandshakeRequest,
                ReceiveMessagesRequest(channels, channelGroups, subscriptionCursor)
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_when_there_is_subscriptionChangedEvent() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val newChannels = listOf("newChannel1")
        val newChannelGroups = listOf("newChannelGroup1")

        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)


        val currentState = State.Handshaking(channels, channelGroups)
        val subscriptionChangedEvent = Event.SubscriptionChanged(newChannels, newChannelGroups, subscriptionCursor)

        //when
        val (handshaking, effectInvocationsForTransitionFromHandshakingToHandshaking) = transition(
            currentState,
            subscriptionChangedEvent
        )

        //then
        assertEquals(State.Handshaking(newChannels, newChannelGroups), handshaking)
        assertThat(
            effectInvocationsForTransitionFromHandshakingToHandshaking,
            Matchers.contains(
                CancelPendingHandshakeRequest,
                HandshakeRequest(newChannels, newChannelGroups)
            )
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_RECONNECTING_when_there_is_handshakingFailureEvent() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        val currentState = State.Handshaking(channels, channelGroups)
        val handshakingFailEvent = Event.HandshakeFailure()

        //when
        val (handshakingReconnecting, effectInvocationsForTransitionFromHandshakingToHandshakingReconnecting) = transition(
            currentState,
            handshakingFailEvent
        )

        //then
//        assertThat(handshakingReconnecting, instanceOf(State.HandshakingFailed::class.java))
        assertEquals(State.HandshakeReconnecting(channels, channelGroups), handshakingReconnecting)
        assertThat(
            effectInvocationsForTransitionFromHandshakingToHandshakingReconnecting,
            Matchers.contains(
                CancelPendingHandshakeRequest,
                HandshakeReconnect(channels, channelGroups)
            )
        )

    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_STOPPED_when_there_is_disconnectEvent() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        val currentState = State.Handshaking(channels, channelGroups)
        val handshakingFailEvent = Event.Disconnect()

        //when
        val (handshakingStopped, effectInvocationsForTransitionFromHandshakingToHandshakingStopped) = transition(
            currentState,
            handshakingFailEvent
        )

        //then
        assertThat(handshakingStopped, instanceOf(State.HandshakeStopped::class.java))
        assertThat(
            effectInvocationsForTransitionFromHandshakingToHandshakingStopped,
            Matchers.contains(
                CancelPendingHandshakeRequest,
            )
        )

    }


}
