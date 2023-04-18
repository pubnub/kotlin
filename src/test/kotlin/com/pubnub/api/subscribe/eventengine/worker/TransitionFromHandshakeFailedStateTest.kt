package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHandshakeFailedStateTest {
    val channels = listOf("Channel1")
    val channelGroups = listOf("ChannelGroup1")
    val exception = PubNubException("Test")
    val timetoken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timetoken, region)

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKE_RECONNECTING_when_there_is_HANDSHAKE_RECONNECT_RETRY_Event() {
        // when
        val (state, invocations) = transition(
            State.HandshakeFailed(channels, channelGroups, exception), Event.HandshakeReconnectRetry
        )

        // then
        assertEquals(State.HandshakeReconnecting(channels, channelGroups, 0, exception), state)
        assertEquals(
            listOf(EffectInvocation.HandshakeReconnect(channels, channelGroups, 0, exception)), invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKE_RECONNECTING_when_there_is_SUBSCRIPTION_CHANGED_Event() {
        // when
        val (state, invocations) = transition(
            State.HandshakeFailed(channels, channelGroups, exception),
            Event.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertEquals(State.HandshakeReconnecting(channels, channelGroups, 0, exception), state)
        assertEquals(
            listOf(EffectInvocation.HandshakeReconnect(channels, channelGroups, 0, exception)), invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_RECEIVE_RECONNECTING_when_there_is_SUBSCRIPTION_RESTORED_Event() {
        // when
        val (state, invocations) = transition(
            State.HandshakeFailed(channels, channelGroups, exception),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(
            State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, exception), state
        )
        assertEquals(
            listOf(EffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 0, exception)),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKE_RECONNECTING_when_there_is_RECONNECT_Event() {
        // when
        val (state, invocations) = transition(
            State.HandshakeFailed(channels, channelGroups, exception), Event.Reconnect
        )

        // then
        assertEquals(State.HandshakeReconnecting(channels, channelGroups, 0, exception), state)
        assertEquals(
            listOf(EffectInvocation.HandshakeReconnect(channels, channelGroups, 0, exception)), invocations
        )
    }
}
