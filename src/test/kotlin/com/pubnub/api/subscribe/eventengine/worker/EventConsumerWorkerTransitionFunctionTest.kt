package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Test

class EventConsumerWorkerTransitionFunctionTest {

    @Test
    fun can_transit_from_state_UNSUBSCRIBED_to_HANDSHAKING_on_SubscriptionChange_event_then_from_HANDSHAKING_to_RECEIVING_on_HandshakingSuccess() {
        // given
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        val subscriptionChangeEvent = Event.SubscriptionChanged(channels, channelGroups)
        val messages = listOf<PNEvent>()

        // when
        val (handshaking, effectInvocationsForSubscriptionChange) = transition(State.Unsubscribed, subscriptionChangeEvent)
        val (receiving01, effectInvocationsForHandshakingSuccess) = transition(
            handshaking, Event.HandshakeSuccess(subscriptionCursor)
        )
        val (_, effectInvocationsForReceivingSuccess) = transition(
            receiving01,
            Event.ReceiveSuccess(messages, subscriptionCursor)
        )

        // then
        assertThat(
            effectInvocationsForSubscriptionChange + effectInvocationsForHandshakingSuccess + effectInvocationsForReceivingSuccess,
            contains(
                SubscribeEffectInvocation.Handshake(channels, channelGroups),
                SubscribeEffectInvocation.CancelHandshake,
                SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNConnectedCategory),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitMessages(listOf()), // toDO brakuje listy Messagy
                SubscribeEffectInvocation.EmitStatus(PNStatusCategory.PNConnectedCategory),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            )
        )
    }
}
