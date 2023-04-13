package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EventConsumerWorkerTransitionFunctionTest {

    @Test
    fun can_transit_from_UNSUBSRIBED_to_HANDSHAKING_when_there_is_SubscriptionChangeEvent() {
        //given
        val currentState: State = State.Unsubscribed
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        val subscriptionChangeEvent = Event.SubscriptionChanged(channels, channelGroups, subscriptionCursor)

        //when
        val transitionResult: Pair<State, List<EffectInvocation>> = transition(currentState, subscriptionChangeEvent)

        //then
        val stateAfterTransition = transitionResult.first
        val transitionEffects = transitionResult.second
        assertThat(stateAfterTransition, Matchers.instanceOf(State.Handshaking::class.java))
        assertEquals(
            listOf<EffectInvocation>(EffectInvocation.HandshakeRequest(channels, channelGroups)),
            transitionEffects
        )
    }


    @Test
    fun can_transit_from_state_UNSUBSCRIBED_to_HANDSHAKING_on_SubscriptionChange_event_then_from_HANDSHAKING_to_RECEIVING_on_HandshakingSuccess() {
        //given
        val currentState: State = State.Unsubscribed
        val channels = listOf("Channel1")
        val channelGroups = listOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        val subscriptionChangeEvent = Event.SubscriptionChanged(channels, channelGroups, subscriptionCursor)
        val messages = listOf<PNEvent>()

        //when
        val (handshaking, effectInvocationsForSubscriptionChange) = transition(currentState, subscriptionChangeEvent)
        val (receiving01, effectInvocationsForHandshakingSuccess) = transition(
            handshaking, Event.HandshakeSuccess(
                SubscriptionCursor(
                    42L,
                    "42"
                )
            )
        )
        val (receiving02, effectInvocationsForReceivingSuccess) = transition(
            receiving01,
            Event.ReceiveSuccess(messages, SubscriptionCursor(42L, "42"))
        )


        //then
        assertThat(
            effectInvocationsForSubscriptionChange + effectInvocationsForHandshakingSuccess + effectInvocationsForReceivingSuccess,
            contains(
                EffectInvocation.HandshakeRequest(channels, channelGroups),
                EffectInvocation.CancelPendingHandshakeRequest,
                EffectInvocation.EmitStatus("Connected"),
                EffectInvocation.ReceiveMessagesRequest(channels, channelGroups, SubscriptionCursor(42L, "42")),
                EffectInvocation.CancelPendingReceiveMessagesRequest,
                EffectInvocation.EmitMessages(listOf()),  //brakuje listy Messagy
                EffectInvocation.ReceiveMessagesRequest(channels, channelGroups, SubscriptionCursor(42L, "42"))

            )
        )

    }

}

