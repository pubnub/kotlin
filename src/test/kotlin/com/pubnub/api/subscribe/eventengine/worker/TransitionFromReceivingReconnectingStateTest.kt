package com.pubnub.api.subscribe.eventengine.worker

import com.google.gson.JsonObject
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromReceivingReconnectingStateTest {
    val channels = listOf("Channel1")
    val channelGroups = listOf("ChannelGroup1")
    val reason = PubNubException(PubNubError.PARSING_ERROR)
    val timeToken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timeToken, region)
    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_RECONNECTING_when_there_is_RECEIVE_RECONNECT_FAILURE_Event() {
        // when
        val (state, invocations) = transition(
            State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            Event.ReceiveReconnectFailure(reason)
        )

        // then
        assertEquals(State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 1, reason), state)
        assertEquals(
            listOf(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 1, reason),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_RECONNECTING_when_there_is_SUBSCRIPTION_CHANGED_Event() {
        // when
        val (state, invocations) = transition(
            State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            Event.SubscriptionChanged(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason), state)
        assertEquals(
            listOf(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 0, reason),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_STOPPED_when_there_is_DISCONNECT_Event() {
        // when
        val (state, invocations) = transition(
            State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            Event.Disconnect
        )

        // then
        assertEquals(State.ReceiveStopped(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                EffectInvocation.CancelReceiveReconnect,
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_FAILED_when_there_is_RECEIVE_RECONNECT_GIVEUP_Event() {
        // when
        val (state, invocations) = transition(
            State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            Event.ReceiveReconnectGiveUp(reason)
        )

        // then
        assertEquals(State.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason), state)
        assertEquals(
            listOf(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.EmitStatus(PNStatusCategory.PNDisconnectedCategory)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVING_when_there_is_RECEIVE_RECONNECT_SUCCESS_Event() {
        // given
        val pnMessageResult: PNEvent = createPnMessageResult(channels.first())
        val messages: List<PNEvent> = listOf(pnMessageResult)

        // when
        val (state, invocations) = transition(
            State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            Event.ReceiveReconnectSuccess(messages, subscriptionCursor)
        )

        // then
        assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.EmitMessages(messages),
                EffectInvocation.EmitStatus(PNStatusCategory.PNConnectedCategory),
                EffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_RECONNECTING_when_there_is_SUBSCRIPTION_RESTORED_Event() {
        // when
        val (state, invocations) = transition(
            State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(State.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason), state)
        assertEquals(
            listOf(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 0, reason),
            ),
            invocations
        )
    }

    private fun createPnMessageResult(channel1: String): PNMessageResult {
        val pubSubResult = BasePubSubResult(
            channel = channel1,
            subscription = null,
            timetoken = 16814672398636798,
            userMetadata = null,
            publisher = "client-d4d5bdeb-02b7-4505-bfc0-82bad22057d6"
        )
        val message = JsonObject()
        message.addProperty("publisher", "client-6c42e3e2-dd3b-487b-a5bf-c2b6be59a15f")
        message.addProperty("text", "D5631DA5FF")
        message.addProperty("uncd", "-!?+=")

        return PNMessageResult(pubSubResult, message)
    }
}
