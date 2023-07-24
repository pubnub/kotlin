package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.eventengine.transition
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromReceiveHeartbeatStoppedStateTest {
    private val channels = listOf("Channel1")
    private val channelGroups = listOf("ChannelGroup1")
    private val timeToken = 12345345452L
    private val region = "42"
    private val subscriptionCursor = SubscriptionCursor(timeToken, region)

    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_RECEIVING_when_there_is_RECONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            SubscribeEvent.Reconnect
        )

        // then
        assertEquals(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
            state
        )
        assertEquals(
            listOf(
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
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
    fun can_transit_from_RECEIVE_STOPPED_to_RECEIVING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            SubscribeEvent.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(listOf(SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)), invocations)
    }

    @Test
    fun can_transit_from_RECEIVE_STOPPED_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor),
            SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(listOf(SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)), invocations)
    }
}
