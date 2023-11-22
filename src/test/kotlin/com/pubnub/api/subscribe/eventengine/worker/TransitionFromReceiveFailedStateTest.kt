package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.transition
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class TransitionFromReceiveFailedStateTest {
    val channels = setOf("Channel1")
    val channelGroups = setOf("ChannelGroup1")
    val exception = PubNubException("Test")
    val timetoken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timetoken, region)
    val reason = PubNubException("Test")

    @Test
    fun `channel and channelGroup should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val receiveFailed: SubscribeState.ReceiveFailed = SubscribeState.ReceiveFailed(myMutableSetOfChannels, myMutableSetOfChannelGroups, subscriptionCursor, reason)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(receiveFailed.channels.contains(channelName))
        assertTrue(receiveFailed.channelGroups.contains(channelGroupName))
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_HANDSHAKING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (state, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            SubscribeEvent.SubscriptionChanged(newChannels, newChannelGroup)
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking // Safe cast

        assertEquals(newChannels, state.channels)
        assertEquals(newChannelGroup, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(newChannels, newChannelGroup)),
            effectInvocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_HANDSHAKING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // given
        val eventChannels = setOf("ChannelZ")
        val eventChannelGroups = setOf("ChannelGroupZ")
        val timetoken = 99945345452L
        val region = "1"
        val eventSubscriptionCursor = SubscriptionCursor(timetoken, region)

        // when
        val (state, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            SubscribeEvent.SubscriptionRestored(eventChannels, eventChannelGroups, eventSubscriptionCursor)
        )
        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking // Safe cast

        assertEquals(eventChannels, state.channels)
        assertEquals(eventChannelGroups, state.channelGroups)
        assertEquals(eventSubscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(eventChannels, eventChannelGroups)),
            effectInvocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_HANDSHAKING_when_there_is_RECONNECT_event() {
        // given
        val timeTokenFromReconnect = 99945345452L
        val subscriptionCursorForReconnect = SubscriptionCursor(timeTokenFromReconnect, null)

        // when
        val (state, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            SubscribeEvent.Reconnect(subscriptionCursorForReconnect)
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursorForReconnect, state.subscriptionCursor)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(channels, channelGroups)),
            effectInvocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_HANDSHAKING_when_there_is_RECONNECT_event_with_reconnectCursor() {
        // when
        val (state, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            SubscribeEvent.Reconnect()
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(channels, channelGroups)),
            effectInvocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_UNSUBSRIBED_when_there_is_UNSUBSRIBED_ALL_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            SubscribeEvent.UnsubscribeAll
        )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(0, invocations.size)
    }
}
