package com.pubnub.internal.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.internal.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TransitionFromHandshakingReconnectingStateTest {
    private val channels = setOf("Channel1")
    private val channelGroups = setOf("ChannelGroup1")
    private val timeToken = 12345345452L
    private val reason = PubNubException("Test")

    @Test
    fun `channel and channelGroup should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val handshakeReconnecting: SubscribeState.HandshakeReconnecting = SubscribeState.HandshakeReconnecting(myMutableSetOfChannels, myMutableSetOfChannelGroups, 0, reason)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(handshakeReconnecting.channels.contains(channelName))
        assertTrue(handshakeReconnecting.channelGroups.contains(channelGroupName))
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_RECONNECTING_when_there_is_HANDSHAKE_RECONNECT_FAILURE_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.HandshakeReconnectFailure(reason)
        )

        // then
        assertTrue(state is SubscribeState.HandshakeReconnecting)
        state as SubscribeState.HandshakeReconnecting

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(1, state.attempts)
        assertEquals(reason, state.reason)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, 1, reason)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.Handshake(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_STOPPED_when_there_is_DISCONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason), SubscribeEvent.Disconnect
        )

        // then
        assertTrue(state is SubscribeState.HandshakeStopped)
        state as SubscribeState.HandshakeStopped

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(reason, state.reason)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_FAILED_when_there_is_HANDSHAKE_RECONNECT_GIVEUP_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.HandshakeReconnectGiveup(reason)
        )

        // then
        assertTrue(state is SubscribeState.HandshakeFailed)
        state as SubscribeState.HandshakeFailed

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(reason, state.reason)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.EmitStatus(PNStatus.ConnectionError(reason))
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_RECEIVING_when_there_is_HANDSHAKE_RECONNECT_SUCCESS_event() {
        // given
        val subscriptionCursorStoredInHandshakeReconnecting = SubscriptionCursor(timeToken, null)
        val regionStoredInStoredInHandshakeReconnectSuccess = "12"
        val timeTokenFromSubscriptionRestored = 99945345452L
        val subscriptionCursorStoredInHandshakeReconnectSuccess = SubscriptionCursor(timeTokenFromSubscriptionRestored, regionStoredInStoredInHandshakeReconnectSuccess)

        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason, subscriptionCursorStoredInHandshakeReconnecting),
            SubscribeEvent.HandshakeReconnectSuccess(subscriptionCursorStoredInHandshakeReconnectSuccess)
        )

        // then
        val expectedSubscriptionCursor = SubscriptionCursor(timeToken, regionStoredInStoredInHandshakeReconnectSuccess)
        assertTrue(state is SubscribeState.Receiving)
        state as SubscribeState.Receiving

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(expectedSubscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus.Connected(timeToken, channels = channels.toList(), channelGroups = channelGroups.toList())
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, expectedSubscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // given
        val subscriptionCursorStoredInHandshakeReconnecting = SubscriptionCursor(timeToken, "12")
        val timeTokenFromSubscriptionRestored = 99945345452L
        val subscriptionCursorStoredInSubscriptionRestored = SubscriptionCursor(timeTokenFromSubscriptionRestored, "1")

        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason, subscriptionCursorStoredInHandshakeReconnecting),
            SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursorStoredInSubscriptionRestored)
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursorStoredInSubscriptionRestored, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.Handshake(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_UNSUBSRIBED_when_there_is_UNSUBSRIBED_ALL_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.UnsubscribeAll
        )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(setOf(SubscribeEffectInvocation.CancelHandshakeReconnect), invocations)
    }
}
