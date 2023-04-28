package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
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
    val channels = listOf("Channel1")
    val channelGroups = listOf("ChannelGroup1")
    val timeToken = 12345345452L
    val region = "42"
    val subscriptionCursor = SubscriptionCursor(timeToken, region)
    val reason = PubNubException("test")

    @Test
    fun can_transit_from_HANDSHAKING_to_RECEIVING_when_there_is_handshakeSuccessEvent() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups), Event.HandshakeSuccess(subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                CancelHandshake,
                EmitStatus(PNStatusCategory.PNConnectedCategory),
                ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_RECEIVING_when_there_is_subscriptionRestoredEvent() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups),
            Event.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            listOf(
                CancelHandshake, ReceiveMessages(channels, channelGroups, subscriptionCursor)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_when_there_is_subscriptionChangedEvent() {
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
                CancelHandshake, Handshake(newChannels, newChannelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_RECONNECTING_when_there_is_handshakingFailureEvent() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups), Event.HandshakeFailure(reason)
        )

        // then
        assertEquals(SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason), state)
        assertEquals(
            listOf(
                CancelHandshake, HandshakeReconnect(channels, channelGroups, 0, reason)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_STOPPED_when_there_is_disconnectEvent() {
        // when
        val (state, invocations) = transition(
            SubscribeState.Handshaking(channels, channelGroups), Event.Disconnect
        )

        // then
        assertThat(state, instanceOf(SubscribeState.HandshakeStopped::class.java))
        assertEquals(
            listOf(
                CancelHandshake,
            ),
            invocations
        )
    }
}
