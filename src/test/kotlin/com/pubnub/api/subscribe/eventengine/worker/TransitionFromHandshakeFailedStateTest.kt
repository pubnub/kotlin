package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.transition
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHandshakeFailedStateTest {
    val channels = setOf("Channel1")
    val channelGroups = setOf("ChannelGroup1")
    val exception = PubNubException("Test")
    val timetoken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timetoken, region)

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKE_RECONNECTING_when_there_is_HANDSHAKE_RECONNECT_RETRY_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception), SubscribeEvent.HandshakeReconnectRetry
        )

        // then
        assertEquals(SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, exception), state)
        assertEquals(
            setOf(SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, 0, exception)), invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception),
            SubscribeEvent.SubscriptionChanged(newChannels, newChannelGroup)
        )

        // then
        assertEquals(SubscribeState.Handshaking(newChannels, newChannelGroup), state)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(newChannels, newChannelGroup)), invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception),
            SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state
        )
        assertEquals(
            setOf(SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKING_when_there_is_RECONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception), SubscribeEvent.Reconnect
        )

        // then
        assertEquals(SubscribeState.Handshaking(channels, channelGroups), state)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(channels, channelGroups)), invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_FAILED_to_UNSUBSRIBED_when_there_is_UNSUBSCRIBE_ALL_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception),
            SubscribeEvent.UnsubscribeAll
        )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(0, invocations.size)
    }
}
