package com.pubnub.internal.subscribe.eventengine.worker

import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.internal.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SubscribeEventConsumerWorkerTransitionFunctionTest {

    @Test
    fun can_transit_from_state_UNSUBSCRIBED_to_HANDSHAKING_on_SubscriptionChange_event_then_from_HANDSHAKING_to_RECEIVING_on_HandshakingSuccess() {
        // given
        val channels = setOf("Channel1")
        val channelGroups = setOf("ChannelGroup1")
        val timeToken = 12345345452L
        val region = "42"
        val subscriptionCursor = SubscriptionCursor(timeToken, region)

        val subscriptionChangeSubscribeEvent = SubscribeEvent.SubscriptionChanged(channels, channelGroups)
        val messages = listOf<PNEvent>()

        // when
        val (handshaking, effectInvocationsForSubscriptionChange) = transition(
            SubscribeState.Unsubscribed,
            subscriptionChangeSubscribeEvent
        )
        val (receiving01, effectInvocationsForHandshakingSuccess) = transition(
            handshaking, SubscribeEvent.HandshakeSuccess(subscriptionCursor)
        )
        val (_, effectInvocationsForReceivingSuccess) = transition(
            receiving01,
            SubscribeEvent.ReceiveSuccess(messages, subscriptionCursor)
        )

        // then
        Assertions.assertEquals(
            setOf(
                SubscribeEffectInvocation.Handshake(channels, channelGroups),
                SubscribeEffectInvocation.CancelHandshake,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(PNStatusCategory.Connected, currentTimetoken = timeToken, channels = channels.toList(), channelGroups = channelGroups.toList())
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitMessages(listOf()),
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(PNStatusCategory.Connected, currentTimetoken = timeToken, channels = channels.toList(), channelGroups = channelGroups.toList())
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            effectInvocationsForSubscriptionChange + effectInvocationsForHandshakingSuccess + effectInvocationsForReceivingSuccess
        )
    }
}
