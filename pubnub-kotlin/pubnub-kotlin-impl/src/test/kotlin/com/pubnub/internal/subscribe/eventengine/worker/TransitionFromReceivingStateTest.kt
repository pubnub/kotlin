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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromReceivingStateTest {
    private val channels = setOf("Channel1")
    private val channelGroups = setOf("ChannelGroup1")
    private val timeToken = 12345345452L
    private val region = "42"
    private val subscriptionCursor = SubscriptionCursor(timeToken, region)
    private val reason = PubNubException(PubNubError.PARSING_ERROR)

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVE_FAILED_when_there_is_RECEIVE_FAILURE_event() {
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
                SubscribeEvent.ReceiveFailure(reason),
            )

        // then
        Assertions.assertTrue(state is SubscribeState.ReceiveFailed)
        state as SubscribeState.ReceiveFailed

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(reason, state.reason)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        category = PNStatusCategory.PNUnexpectedDisconnectCategory,
                        exception = reason,
                    ),
                ),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVE_STOPPED_when_there_is_DISCONNECT_event() {
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
                SubscribeEvent.Disconnect,
            )

        // then
        Assertions.assertTrue(state is SubscribeState.ReceiveStopped)
        state as SubscribeState.ReceiveStopped

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(PNStatusCategory.PNDisconnectedCategory),
                ),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_SUBSCRIPTION_CHANGED_event_with_different_channels_and_groups() {
        // given
        val newChannels = setOf("Channel2", "Channel3")
        val newChannelGroups = setOf("ChannelGroup2")

        // when
        val (state, invocations) =
            transition(
                SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
                SubscribeEvent.SubscriptionChanged(newChannels, newChannelGroups),
            )

        // then
        Assertions.assertTrue(state is SubscribeState.Receiving)
        state as SubscribeState.Receiving

        assertEquals(newChannels, state.channels)
        assertEquals(newChannelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitStatus(
                    createSubscriptionChangedStatus(
                        state.subscriptionCursor,
                        newChannels,
                        newChannelGroups,
                    ),
                ),
                SubscribeEffectInvocation.ReceiveMessages(newChannels, newChannelGroups, subscriptionCursor),
            ),
            invocations,
        )
    }

    @Test
    fun stays_in_RECEIVING_and_emits_status_without_resubscribing_when_SUBSCRIPTION_CHANGED_event_has_same_channels_and_groups() {
        // given - channels and channelGroups are the same
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
                SubscribeEvent.SubscriptionChanged(channels, channelGroups),
            )

        // then
        Assertions.assertTrue(state is SubscribeState.Receiving)
        state as SubscribeState.Receiving

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        // Should only emit status - no cancel or new receive
        assertEquals(
            setOf(
                SubscribeEffectInvocation.EmitStatus(
                    createSubscriptionChangedStatus(
                        state.subscriptionCursor,
                        channels,
                        channelGroups,
                    ),
                ),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // given
        val regionStoredInStoredInReceive = "12"
        val subscriptionCursorStoredInReceiving = SubscriptionCursor(timeToken, regionStoredInStoredInReceive)
        val timeTokenFromSubscriptionRestored = 99945345452L
        val subscriptionCursorStoredInSubscriptionRestored = SubscriptionCursor(timeTokenFromSubscriptionRestored, null)

        // when
        val (state, invocations) =
            transition(
                SubscribeState.Receiving(channels, channelGroups, subscriptionCursorStoredInReceiving),
                SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursorStoredInSubscriptionRestored),
            )

        // then
        val expectedSubscriptionCursor =
            SubscriptionCursor(timeTokenFromSubscriptionRestored, regionStoredInStoredInReceive)
        Assertions.assertTrue(state is SubscribeState.Receiving)
        state as SubscribeState.Receiving

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(expectedSubscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitStatus(
                    createSubscriptionChangedStatus(
                        state.subscriptionCursor,
                        channels,
                        channelGroups,
                    ),
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, expectedSubscriptionCursor),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_RECEIVE_SUCCESS_event() {
        // given
        val pnMessageResult: PNEvent = createPnMessageResult(channels.first())
        val messages: List<PNEvent> = listOf(pnMessageResult)

        // when
        val (state, invocations) =
            transition(
                SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
                SubscribeEvent.ReceiveSuccess(messages, subscriptionCursor),
            )

        // then
        Assertions.assertTrue(state is SubscribeState.Receiving)
        state as SubscribeState.Receiving

        assertEquals(channels, state.channels)
        assertEquals(channelGroups, state.channelGroups)
        assertEquals(subscriptionCursor, state.subscriptionCursor)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitMessages(messages),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_UNSUBSRIBED_when_there_is_UNSUBSCRIBE_ALL_event() {
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Receiving(channels, channelGroups, subscriptionCursor),
                SubscribeEvent.UnsubscribeAll,
            )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(PNStatusCategory.PNDisconnectedCategory),
                ),
            ),
            invocations,
        )
    }

    private fun createPnMessageResult(channel1: String): PNMessageResult {
        val pubSubResult =
            BasePubSubResult(
                channel = channel1,
                subscription = null,
                timetoken = 16814672398636798,
                userMetadata = null,
                publisher = "client-d4d5bdeb-02b7-4505-bfc0-82bad22057d6",
            )
        val message =
            JsonObject().apply {
                addProperty("publisher", "client-6c42e3e2-dd3b-487b-a5bf-c2b6be59a15f")
                addProperty("text", "D5631DA5FF")
                addProperty("uncd", "-!?+=")
            }

        return PNMessageResult(pubSubResult, message)
    }
}

internal fun createSubscriptionChangedStatus(
    cursor: SubscriptionCursor,
    channels: Collection<String>,
    channelGroups: Collection<String>,
) = PNStatus(
    PNStatusCategory.PNSubscriptionChanged,
    currentTimetoken = cursor.timetoken,
    affectedChannels = channels,
    affectedChannelGroups = channelGroups,
)
