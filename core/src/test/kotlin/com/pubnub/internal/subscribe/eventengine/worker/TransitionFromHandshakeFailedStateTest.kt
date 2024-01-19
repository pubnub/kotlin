package com.pubnub.internal.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.internal.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class TransitionFromHandshakeFailedStateTest {
    val channels = setOf("Channel1")
    val channelGroups = setOf("ChannelGroup1")
    val exception = PubNubException("Test")
    val timetoken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timetoken, region)

    @Test
    fun `channel and channelGroup should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val handshakeFailed: SubscribeState.HandshakeFailed = SubscribeState.HandshakeFailed(myMutableSetOfChannels, myMutableSetOfChannelGroups, exception)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(handshakeFailed.channels.contains(channelName))
        assertTrue(handshakeFailed.channelGroups.contains(channelGroupName))
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
        assertTrue(state is SubscribeState.Handshaking)
        val handshaking = state as SubscribeState.Handshaking
        assertEquals(newChannels, handshaking.channels)
        assertEquals(newChannelGroup, handshaking.channelGroups)
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
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(channels, channelGroups)),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKING_when_there_is_RECONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception), SubscribeEvent.Reconnect()
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(channels, channelGroups)), invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_FAILED_to_HANDSHAKING_when_there_is_RECONNECT_event_with_timeToken() {
        // given
        val timeTokenFromReconnect = 99945345452L
        val subscriptionCursorForReconnect = SubscriptionCursor(timeTokenFromReconnect, null)

        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeFailed(channels, channelGroups, exception), SubscribeEvent.Reconnect(subscriptionCursorForReconnect)
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        val handshaking = state as SubscribeState.Handshaking
        assertEquals(channels, handshaking.channels)
        assertEquals(channelGroups, handshaking.channelGroups)
        assertEquals(subscriptionCursorForReconnect, handshaking.subscriptionCursor)
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
