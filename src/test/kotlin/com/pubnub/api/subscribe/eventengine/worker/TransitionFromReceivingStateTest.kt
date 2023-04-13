package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubError
import com.pubnub.api.models.consumer.pubsub.PNEvent
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

class TransitionFromReceivingStateTest {

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_RECONNECTING_when_there_is_RECEIVE_FAILURE_Event() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)
        val reason = PubNubError.PARSING_ERROR

        //when
        val (receivingReconnecting, effectInvocationsForTransitionFromReceivingToReceivingReconnecting) = transition(
            State.Receiving(channels, channelGroups, subscriptionCursor),
            Event.ReceiveFailure(channels, channelGroups, reason)
        )

        //then
        Assert.assertEquals(State.ReceiveReconnecting(channels, channelGroups), receivingReconnecting)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceivingToReceivingReconnecting,
            Matchers.contains(
                EffectInvocation.CancelReceiveEvents,
                EffectInvocation.ReceiveReconnect(channels, channelGroups),
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVE_STOPPED_when_there_is_DISCONNECT_Event() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)


        //when
        val (receiveStopped, effectInvocationsForTransitionFromReceivingToReceiveStopped) = transition(
            State.Receiving(channels, channelGroups, subscriptionCursor),
            Event.Disconnect()
        )

        //then
        MatcherAssert.assertThat(receiveStopped, IsInstanceOf.instanceOf(State.ReceiveStopped::class.java))
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceivingToReceiveStopped,
            Matchers.contains(
                EffectInvocation.CancelReceiveEvents,
                EffectInvocation.EmitStatus("Disconnected")
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_SUBSCRIPTION_CHANGED_Event() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)


        //when
        val (receiving, effectInvocationsForTransitionFromReceivingToReceiveReconnectGiveUp) = transition(
            State.Receiving(channels, channelGroups, subscriptionCursor),
            Event.SubscriptionChanged(channels, channelGroups, subscriptionCursor)
        )

        //then
        Assert.assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceivingToReceiveReconnectGiveUp,
            Matchers.contains(
                EffectInvocation.CancelReceiveEvents,
                EffectInvocation.ReceiveMessagesRequest(channels, channelGroups, subscriptionCursor),
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_Event() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)


        //when
        val (receiving, effectInvocationsForTransitionFromReceivingToReceiveReconnectGiveUp) = transition(
            State.Receiving(channels, channelGroups, subscriptionCursor),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        //then
        Assert.assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceivingToReceiveReconnectGiveUp,
            Matchers.contains(
                EffectInvocation.CancelReceiveEvents,
                EffectInvocation.ReceiveMessagesRequest(channels, channelGroups, subscriptionCursor),
            )
        )
    }

    @Test
    fun can_transit_from_RECEIVING_to_RECEIVING_when_there_is_RECEIVE_SUCCESS_Event() {
        //given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)
        val messages: List<PNEvent> = listOf() //<-- todo add elements here


        //when
        val (receiving, effectInvocationsForTransitionFromReceivingToReceiveReconnectGiveUp) = transition(
            State.Receiving(channels, channelGroups, subscriptionCursor),
            Event.ReceiveSuccess(messages, subscriptionCursor)
        )

        //then
        Assert.assertEquals(State.Receiving(channels, channelGroups, subscriptionCursor), receiving)
        MatcherAssert.assertThat(
            effectInvocationsForTransitionFromReceivingToReceiveReconnectGiveUp,
            Matchers.contains(
                EffectInvocation.CancelReceiveEvents,
                EffectInvocation.EmitStatus("Connected"),
                EffectInvocation.ReceiveMessagesRequest(channels, channelGroups, subscriptionCursor),
            )
        )
    }



}