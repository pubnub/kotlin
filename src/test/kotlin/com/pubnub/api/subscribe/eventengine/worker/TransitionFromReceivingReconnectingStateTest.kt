package com.pubnub.api.subscribe.eventengine.worker

import com.google.gson.JsonObject
import com.pubnub.api.PubNubError
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.jupiter.api.Test

class TransitionFromReceivingReconnectingStateTest {
    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_RECONNECTING_when_there_is_RECEIVE_RECONNECT_FAILURE_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val reason = PubNubError.PARSING_ERROR

        // when
        val (receiveReconnecting, effectInvocationsForTransitionFromReceiveReconnectingToReceiveReconnecting) = transition(
            State.ReceiveReconnecting(channels, channelGroups),
            Event.ReceiveReconnectFailure(channels, channelGroups, reason)
        )

        // then
        Assert.assertEquals(State.ReceiveReconnecting(channels, channelGroups), receiveReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceiveReconnectingToReceiveReconnecting,
            Matchers.contains(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.ReceiveReconnect(channels, channelGroups),
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_RECONNECTING_when_there_is_SUBSCRIPTION_CHANGED_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        // when
        val (receiveReconnecting, effectInvocationsForTransitionFromReceiveReconnectingToReceiveReconnecting) = transition(
            State.ReceiveReconnecting(channels, channelGroups),
            Event.SubscriptionChanged(channels, channelGroups, subscriptionCursor)
        )

        // then
        Assert.assertEquals(State.ReceiveReconnecting(channels, channelGroups), receiveReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceiveReconnectingToReceiveReconnecting,
            Matchers.contains(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.ReceiveReconnect(channels, channelGroups),
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_STOPPED_when_there_is_DISCONNECT_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")

        // when
        val (receiveStopped, effectInvocationsForTransitionFromReceiveReconnectingToReceiveStopped) = transition(
            State.ReceiveReconnecting(channels, channelGroups),
            Event.Disconnect()
        )

        // then
        MatcherAssert.assertThat(receiveStopped, IsInstanceOf.instanceOf(State.ReceiveStopped::class.java))
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceiveReconnectingToReceiveStopped,
            Matchers.contains(
                EffectInvocation.CancelReceiveReconnect,
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_FAILED_when_there_is_RECEIVE_RECONNECT_GIVEUP_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val reason = PubNubError.PARSING_ERROR

        // when
        val (receiveFailed, effectInvocationsForTransitionFromReceiveReconnectingToReceiveFailed) = transition(
            State.ReceiveReconnecting(channels, channelGroups),
            Event.ReceiveReconnectGiveUp(reason)
        )

        // then
        MatcherAssert.assertThat(receiveFailed, IsInstanceOf.instanceOf(State.ReceiveFailed::class.java))
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceiveReconnectingToReceiveFailed,
            Matchers.contains(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.EmitStatus("Disconnected")
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVING_when_there_is_RECEIVE_RECONNECT_SUCCESS_Event() {
        // given
        val channel1 = "Channel1"
        val channels = listOf(channel1)
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)
        val pnMessageResult: PNEvent = createPnMessageResult(channel1)
        val messages: List<PNEvent> = listOf(pnMessageResult)

        // when
        val (receiving, effectInvocationsForTransitionFromReceiveReconnectingToReceiving) = transition(
            State.ReceiveReconnecting(channels, channelGroups),
            Event.ReceiveReconnectSuccess(messages, subscriptionCursor)
        )

        // then
        Assert.assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceiveReconnectingToReceiving,
            Matchers.contains(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.EmitMessages(messages),
                EffectInvocation.EmitStatus("Connected"),
                EffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVE_RECONNECTING_to_RECEIVE_RECONNECTING_when_there_is_SUBSCRIPTION_RESTORED_Event() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        // when
        val (receiveReconnecting, effectInvocationsForTransitionFromReceiveReconnectingToReceiveReconnecting) = transition(
            State.ReceiveReconnecting(channels, channelGroups),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        Assert.assertEquals(State.ReceiveReconnecting(channels, channelGroups), receiveReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceiveReconnectingToReceiveReconnecting,
            Matchers.contains(
                EffectInvocation.CancelReceiveReconnect,
                EffectInvocation.ReceiveReconnect(channels, channelGroups),
            )
        )
    }

    private fun createPnMessageResult(channel1: String): PNMessageResult {
        val pubSubResult: BasePubSubResult = BasePubSubResult(
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
