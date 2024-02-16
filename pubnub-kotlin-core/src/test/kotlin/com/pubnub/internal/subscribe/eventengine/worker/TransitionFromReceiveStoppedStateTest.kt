package com.pubnub.internal.subscribe.eventengine.worker

import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.internal.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TransitionFromReceiveStoppedStateTest {
    private val channels = setOf("Channel1")
    private val channelGroups = setOf("ChannelGroup1")
    private val timeToken = 12345345452L
    private val region = "42"
    private val subscriptionCursor = SubscriptionCursor(timeToken, region)

    @Test
    fun `channel and channelGroup should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val receiveStopped: SubscribeState.ReceiveStopped =
            SubscribeState.ReceiveStopped(myMutableSetOfChannels, myMutableSetOfChannelGroups, subscriptionCursor)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(receiveStopped.channels.contains(channelName))
        assertTrue(receiveStopped.channelGroups.contains(channelGroupName))
    }

    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_HANDSHAKING_when_there_is_RECONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            SubscribeEvent.Reconnect()
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.Handshake(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_HANDSHAKING_when_there_is_RECONNECT_event_with_reconnectCursor() {
        // given
        val timeTokenFromReconnect = 99945345452L
        val subscriptionCursorForReconnect = SubscriptionCursor(timeTokenFromReconnect, null)

        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            SubscribeEvent.Reconnect(subscriptionCursorForReconnect)
        )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        state as SubscribeState.Handshaking

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursorForReconnect, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.Handshake(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_UNSUBSRIBED_when_there_is_UNSUBSRIBED_ALL_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            SubscribeEvent.UnsubscribeAll
        )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(0, invocations.size)
    }

    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_RECEIVE_STOPPED_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            SubscribeEvent.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertTrue(state is SubscribeState.ReceiveStopped)
        state as SubscribeState.ReceiveStopped

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(0, invocations.size)
    }

    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_RECEIVE_STOPPED_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertTrue(state is SubscribeState.ReceiveStopped)
        state as SubscribeState.ReceiveStopped

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(0, invocations.size)
    }
}
