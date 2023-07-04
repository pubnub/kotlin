package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation.CancelHandshake
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation.EmitStatus
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation.Handshake
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation.HandshakeReconnect
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation.ReceiveMessages
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import com.pubnub.api.subscribe.eventengine.transition.transition
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHandshakingStateTest {
    private val channels = listOf("Channel1")
    private val channelGroups = listOf("ChannelGroup1")
    private val timeToken = 12345345452L
    private val region = "42"
    private val subscriptionCursor = SubscriptionCursor(timeToken, region)
    private val reason = PubNubException("test")

    @Test
    fun can_transit_from_HANDSHAKING_to_RECEIVING_when_there_is_HANDSHAKE_SUCCESS_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups), Event.HandshakeSuccess(subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                CancelHandshake,
                EmitStatus(
                    PNStatus(
                        category = PNStatusCategory.PNConnectedCategory,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = false,
                        affectedChannels = channels,
                        affectedChannelGroups = channelGroups
                    )
                ),
                ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                CancelHandshake,
                ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // given
        val newChannels = listOf("newChannel1")
        val newChannelGroups = listOf("newChannelGroup1")

        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups),
            Event.SubscriptionChanged(newChannels, newChannelGroups)
        )

        // then
        assertEquals(SubscribeState.Handshaking(newChannels, newChannelGroups), state)
        assertEquals(
            listOf(
                CancelHandshake,
                Handshake(newChannels, newChannelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_RECONNECTING_when_there_is_HANDSHAKING_FAILURE_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups), Event.HandshakeFailure(reason)
        )

        // then
        assertEquals(SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason), state)
        assertEquals(
            listOf(
                CancelHandshake,
                HandshakeReconnect(channels, channelGroups, 0, reason)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_STOPPED_when_there_is_DISCONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups),
            Event.Disconnect
        )

        // then
        assertThat(state, instanceOf(SubscribeState.HandshakeStopped::class.java))
        assertEquals(listOf(CancelHandshake), invocations)
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_UNSUBSRIBED_when_there_is_UNSUBSCRIBE_ALL_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups),
            Event.UnsubscribeAll
        )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(listOf(CancelHandshake), invocations)
    }
}
