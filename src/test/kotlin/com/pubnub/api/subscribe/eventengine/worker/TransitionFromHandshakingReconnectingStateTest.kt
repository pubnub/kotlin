package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.transition
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransitionFromHandshakingReconnectingStateTest {
    private val channels = setOf("Channel1")
    private val channelGroups = setOf("ChannelGroup1")
    private val timeToken = 12345345452L
    private val region = "42"
    private val subscriptionCursor = SubscriptionCursor(timeToken, region)
    private val reason = PubNubException("Test")

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_RECONNECTING_when_there_is_HANDSHAKE_RECONNECT_FAILURE_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.HandshakeReconnectFailure(reason)
        )

        // then
        assertEquals(SubscribeState.HandshakeReconnecting(channels, channelGroups, 1, reason), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, 1, reason)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.SubscriptionChanged(channels, channelGroups)
        )

        // then
        assertEquals(SubscribeState.Handshaking(channels, channelGroups), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.Handshake(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_STOPPED_when_there_is_DISCONNECT_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason), SubscribeEvent.Disconnect
        )

        // then
        assertEquals(SubscribeState.HandshakeStopped(channels, channelGroups, reason), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_HANDSHAKE_FAILED_when_there_is_HANDSHAKE_RECONNECT_GIVEUP_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.HandshakeReconnectGiveup(reason)
        )

        // then
        assertEquals(SubscribeState.HandshakeFailed(channels, channelGroups, reason), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        category = PNStatusCategory.PNConnectionError,
                        operation = PNOperationType.PNSubscribeOperation,
                        error = true,
                        affectedChannels = channels.toList(),
                        affectedChannelGroups = channelGroups.toList(),
                        exception = reason
                    )
                )
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_RECEIVING_when_there_is_HANDSHAKE_RECONNECT_SUCCESS_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.HandshakeReconnectSuccess(subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Receiving(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
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
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_RECEIVING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursor)
        )

        // then
        assertEquals(SubscribeState.Handshaking(channels, channelGroups, subscriptionCursor), state)
        assertEquals(
            setOf(
                SubscribeEffectInvocation.CancelHandshakeReconnect,
                SubscribeEffectInvocation.Handshake(channels, channelGroups)
            ),
            invocations
        )
    }

    @Test
    fun can_transit_from_HANDSHAKE_RECONNECTING_to_UNSUBSRIBED_when_there_is_UNSUBSRIBED_ALL_event() {
        // when
        val (state, invocations) = transition(
            SubscribeState.HandshakeReconnecting(channels, channelGroups, 0, reason),
            SubscribeEvent.UnsubscribeAll
        )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(setOf(SubscribeEffectInvocation.CancelHandshakeReconnect), invocations)
    }
}
