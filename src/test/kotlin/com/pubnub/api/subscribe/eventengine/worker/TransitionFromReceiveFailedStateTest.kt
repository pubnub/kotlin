package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromReceiveFailedStateTest {
    val channels = listOf("Channel1")
    val channelGroups = listOf("ChannelGroup1")
    val exception = PubNubException("Test")
    val timetoken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timetoken, region)
    val reason = PubNubException("Test")

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_RECEIVE_RECONNECTING_when_there_is_RECEIVE_RECONNECT_RETRY_Event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            Event.ReceiveReconnectRetry
        )

        // then
        assertEquals(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason), state
        )
        assertEquals(
            listOf(
                SubscribeEffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 0, reason)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_RECEIVING_when_there_is_SUBSCRIPTION_CHANGED_Event() {
        // given
        val newChannels: List<String> = channels + listOf("NewChannel")
        val newChannelGroup = channelGroups + listOf("NewChannelGroup")

        // when
        val (receivingState, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            Event.SubscriptionChanged(newChannels, newChannelGroup)
        )

        // then
        assertEquals(SubscribeState.Receiving(newChannels, newChannelGroup, subscriptionCursor), receivingState)
        assertEquals(
            listOf(SubscribeEffectInvocation.ReceiveMessages(newChannels, newChannelGroup, subscriptionCursor)),
            effectInvocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_Event() {
        // when
        val (receivingState, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )
        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), receivingState)
        assertEquals(
            listOf(SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)),
            effectInvocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_FAILED_to_RECEIVING_when_there_is_RECONNECT_Event() {
        // when
        val (receivingState, effectInvocations) = transition(
            SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason), Event.Reconnect
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), receivingState)
        assertEquals(
            listOf(SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)),
            effectInvocations
        )
    }
}
