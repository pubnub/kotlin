package com.pubnub.api.subscribe.eventengine.worker

import com.google.gson.JsonObject
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromReceivingStateTest {
    private val channels = listOf("Channel1")
    private val channelGroups = listOf("ChannelGroup1")
    private val timeToken = 12345345452L
    private val region = "42"
    private val subscriptionCursor = SubscriptionCursor(timeToken, region)
    private val reason = PubNubException(PubNubError.PARSING_ERROR)
    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_RECONNECTING_when_there_is_RECEIVE_FAILURE_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
            Event.ReceiveFailure(reason)
        )

        // then
        assertEquals(SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 0, reason)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVE_STOPPED_when_there_is_DISCONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
            Event.Disconnect
        )

        // then
        assertEquals(SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        category = PNStatusCategory.PNDisconnectedCategory,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = false,
                        affectedChannels = channels,
                        affectedChannelGroups = channelGroups
                    )
                )
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // given
        // when
        val (state, invocations) = transition(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
            Event.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_RECEIVE_SUCCESS_event() {
        // given
        val pnMessageResult: PNEvent = createPnMessageResult(channels.first())
        val messages: List<PNEvent> = listOf(pnMessageResult)

        // when
        val (state, invocations) = transition(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
            Event.ReceiveSuccess(messages, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitMessages(messages),
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        category = PNStatusCategory.PNConnectedCategory,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = false,
                        affectedChannels = channels,
                        affectedChannelGroups = channelGroups
                    )
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_UNSUBSRIBED_when_there_is_UNSUBSCRIBE_ALL_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
            Event.UnsubscribeAll
        )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(listOf(SubscribeEffectInvocation.CancelReceiveMessages), invocations)
    }

    private fun createPnMessageResult(channel1: String): PNMessageResult {
        val pubSubResult = BasePubSubResult(
            channel = channel1,
            subscription = null,
            timetoken = 16814672398636798,
            userMetadata = null,
            publisher = "client-d4d5bdeb-02b7-4505-bfc0-82bad22057d6"
        )
        val message = JsonObject().apply {
            addProperty("publisher", "client-6c42e3e2-dd3b-487b-a5bf-c2b6be59a15f")
            addProperty("text", "D5631DA5FF")
            addProperty("uncd", "-!?+=")
        }

        return PNMessageResult(pubSubResult, message)
    }
}
