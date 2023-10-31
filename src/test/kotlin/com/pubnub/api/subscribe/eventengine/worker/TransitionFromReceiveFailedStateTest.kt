package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.transition
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
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
    fun can_transit_from_RECEIVE_FAILED_to_HANDSHAKING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // given
        val newChannels = channels + setOf("NewChannel")
        val newChannelGroup = channelGroups + setOf("NewChannelGroup")

        // when
        val (receivingState, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            SubscribeEvent.SubscriptionChanged(newChannels, newChannelGroup)
        )

        // then
        assertEquals(SubscribeState.Handshaking(newChannels, newChannelGroup, subscriptionCursor), receivingState)
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
        val (receivingState, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            SubscribeEvent.SubscriptionRestored(eventChannels, eventChannelGroups, eventSubscriptionCursor)
        )
        // then
        assertEquals(SubscribeState.Handshaking(eventChannels, eventChannelGroups, eventSubscriptionCursor), receivingState)
        assertEquals(
            setOf(SubscribeEffectInvocation.Handshake(eventChannels, eventChannelGroups)),
            effectInvocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_HANDSHAKING_when_there_is_RECONNECT_event() {
        // when
        val (receivingState, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            SubscribeEvent.Reconnect
        )

        // then
        assertEquals(SubscribeState.Handshaking(channels, channelGroups, subscriptionCursor), receivingState)
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
