package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
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
            SubscribeState.HandshakeFailed(channels, channelGroups, exception), Event.HandshakeReconnectRetry
        )

        // then
        assertEquals(SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, exception), state)
        assertEquals(
            listOf(SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, 0, exception)), invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKING_when_there_is_SUBSCRIPTION_CHANGED_Event() {
        // given
        val newChannels: List<String> = channels + listOf("NewChannel")
        val newChannelGroup = channelGroups + listOf("NewChannelGroup")

        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception),
            Event.SubscriptionChanged(newChannels, newChannelGroup)
        )

        // then
        assertEquals(SubscribeState.Handshaking(newChannels, newChannelGroup), state)
        assertEquals(
            listOf(SubscribeEffectInvocation.Handshake(newChannels, newChannelGroup)), invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_Event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state
        )
        assertEquals(
            listOf(SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKING_when_there_is_RECONNECT_Event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception), Event.Reconnect
        )

        // then
        assertEquals(SubscribeState.Handshaking(channels, channelGroups), state)
        assertEquals(
            listOf(SubscribeEffectInvocation.Handshake(channels, channelGroups)), invocations
        )
    }
}
