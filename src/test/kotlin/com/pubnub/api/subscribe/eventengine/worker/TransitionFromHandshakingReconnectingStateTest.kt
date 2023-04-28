package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHandshakingReconnectingStateTest {
    val channels = listOf("Channel1")
    val channelGroups = listOf("ChannelGroup1")
    val timeToken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timeToken, region)
    val reason = PubNubException("Test")

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_RECONNECTING_when_there_is_HANDSHAKE_RECONNECT_FAILURE_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason), Event.HandshakeReconnectFailure(reason)
        )

        // then
        assertEquals(SubscribeState.HandshakeReconnecting(channels, channelGroups, 1, reason), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, 1, reason)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_RECONNECTING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            Event.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertEquals(SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason), state)

        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, 0, reason),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_STOPPED_when_there_is_DISCONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason), Event.Disconnect
        )

        // then
        assertEquals(SubscribeState.HandshakeStopped(channels, channelGroups, reason), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_FAILED_when_there_is_HANDSHAKE_RECONNECT_GIVEUP_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason), Event.HandshakeReconnectGiveUp(reason)
        )

        // then
        assertEquals(SubscribeState.HandshakeFailed(channels, channelGroups, reason), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_RECEIVING_when_there_is_HANDSHAKE_RECONNECT_SUCCESS_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            Event.HandshakeReconnectSuccess(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNConnectedCategory),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }
}
