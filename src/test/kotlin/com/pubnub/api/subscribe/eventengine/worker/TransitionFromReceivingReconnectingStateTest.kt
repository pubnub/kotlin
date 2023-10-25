package com.pubnub.api.subscribe.eventengine.worker

import com.google.gson.JsonObject
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.transition
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TransitionFromReceivingReconnectingStateTest {
    val channels = setOf("Channel1")
    val channelGroups = setOf("ChannelGroup1")
    val reason = PubNubException(PubNubError.PARSING_ERROR)
    val timeToken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timeToken, region)
    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_RECONNECTING_when_there_is_RECEIVE_RECONNECT_FAILURE_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            SubscribeEvent.ReceiveReconnectFailure(reason)
        )

        // then
        assertEquals(SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 1, reason), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.ReceiveReconnect(channels, channelGroups, subscriptionCursor, 1, reason),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            SubscribeEvent.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_STOPPED_when_there_is_DISCONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            SubscribeEvent.Disconnect
        )

        // then
        assertEquals(SubscribeState.ReceiveStopped(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        category = PNStatusCategory.PNDisconnectedCategory,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = false, // todo is PNDisconnectedCategory error
                        affectedChannels = channels.toList(),
                        affectedChannelGroups = channelGroups.toList()
                    )
                )
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_FAILED_when_there_is_RECEIVE_RECONNECT_GIVEUP_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            SubscribeEvent.ReceiveReconnectGiveup(reason)
        )

        // then
        assertEquals(SubscribeState.ReceiveFailed(channels, channelGroups, subscriptionCursor, reason), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        category = PNStatusCategory.PNUnexpectedDisconnectCategory,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = false,
                        affectedChannels = channels.toList(),
                        affectedChannelGroups = channelGroups.toList()
                    )
                )
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVING_when_there_is_RECEIVE_RECONNECT_SUCCESS_event() {
        // given
        val pnMessageResult: PNEvent = createPnMessageResult(channels.first())
        val messages: List<PNEvent> = listOf(pnMessageResult)

        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            SubscribeEvent.ReceiveReconnectSuccess(messages, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitMessages(messages),
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        category = PNStatusCategory.PNConnectedCategory,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = false,
                        affectedChannels = channels.toList(),
                        affectedChannelGroups = channelGroups.toList()
                    )
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_UNSUBSRIBED_when_there_is_UNSUBSCRIBE_ALL_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            SubscribeEvent.UnsubscribeAll
        )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(setOf(SubscribeEffectInvocation.CancelReceiveReconnect), invocations)
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
