package com.pubnub.internal.subscribe.eventengine.worker

import com.google.gson.JsonObject
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.internal.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class TransitionFromReceivingReconnectingStateTest {
    val channels = setOf("Channel1")
    val channelGroups = setOf("ChannelGroup1")
    val reason = PubNubException(PubNubError.PARSING_ERROR)
    val timeToken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timeToken, region)

    @Test
    fun `channel and channelGroup should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val receiveReconnecting: SubscribeState.ReceiveReconnecting = SubscribeState.ReceiveReconnecting(
            myMutableSetOfChannels,
            myMutableSetOfChannelGroups,
            subscriptionCursor,
            0,
            reason
        )

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(receiveReconnecting.channels.contains(channelName))
        assertTrue(receiveReconnecting.channelGroups.contains(channelGroupName))
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_RECONNECTING_when_there_is_RECEIVE_RECONNECT_FAILURE_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(channels, channelGroups, subscriptionCursor, 0, reason),
            SubscribeEvent.ReceiveReconnectFailure(reason)
        )

        // then
        assertTrue(state is SubscribeState.ReceiveReconnecting)
        val receiveReconnecting = state as SubscribeState.ReceiveReconnecting
        assertEquals(channels, receiveReconnecting.channels)
        assertEquals(channelGroups, receiveReconnecting.channelGroups)
        assertEquals(subscriptionCursor, receiveReconnecting.subscriptionCursor)
        assertEquals(1, receiveReconnecting.attempts)
        assertEquals(reason, receiveReconnecting.reason)
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
        assertTrue(state is SubscribeState.Receiving)
        val handshaking = state as SubscribeState.Receiving
        assertEquals(channels, handshaking.channels)
        assertEquals(channelGroups, handshaking.channelGroups)
        assertEquals(subscriptionCursor, handshaking.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    createSubscriptionChangedStatus(
                        state.subscriptionCursor,
                        channels,
                        channelGroups
                    )
                ),
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
        assertTrue(state is SubscribeState.ReceiveStopped)
        val receiveStopped = state as SubscribeState.ReceiveStopped
        assertEquals(channels, receiveStopped.channels)
        assertEquals(channelGroups, receiveStopped.channelGroups)
        assertEquals(subscriptionCursor, receiveStopped.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(PNStatusCategory.Disconnected)
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
        assertTrue(state is SubscribeState.ReceiveFailed)
        val receiveFailed = state as SubscribeState.ReceiveFailed
        assertEquals(channels, receiveFailed.channels)
        assertEquals(channelGroups, receiveFailed.channelGroups)
        assertEquals(subscriptionCursor, receiveFailed.subscriptionCursor)
        assertEquals(reason, receiveFailed.reason)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(PNStatusCategory.UnexpectedDisconnect, reason)
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
        assertTrue(state is SubscribeState.Receiving)
        state as SubscribeState.Receiving // Smart cast

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitMessages(messages),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // given
        val regionStoredInStoredInReceiveReconnecting = "12"
        val subscriptionCursorStoredInReceiveReconnecting =
            SubscriptionCursor(timeToken, regionStoredInStoredInReceiveReconnecting)
        val timeTokenFromSubscriptionRestored = 99945345452L
        val subscriptionCursorStoredInSubscriptionRestored = SubscriptionCursor(timeTokenFromSubscriptionRestored, null)

        // when
        val (state, invocations) = transition(
            SubscribeState.ReceiveReconnecting(
                channels,
                channelGroups,
                subscriptionCursorStoredInReceiveReconnecting,
                0,
                reason
            ),
            SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursorStoredInSubscriptionRestored)
        )

        // then
        val expectedSubscriptionCursor =
            SubscriptionCursor(timeTokenFromSubscriptionRestored, regionStoredInStoredInReceiveReconnecting)
        assertTrue(state is SubscribeState.Receiving)
        state as SubscribeState.Receiving // Safe cast

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(expectedSubscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    createSubscriptionChangedStatus(
                        state.subscriptionCursor,
                        channels,
                        channelGroups
                    )
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, expectedSubscriptionCursor),
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
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(PNStatusCategory.Disconnected)
                )

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

internal fun createSubscriptionChangedStatus(
    cursor: SubscriptionCursor,
    channels: Collection<String>,
    channelGroups: Collection<String>
) = PNStatus(PNStatusCategory.SubscriptionChanged, currentTimetoken = cursor.timetoken, channels = channels, channelGroups = channelGroups)
