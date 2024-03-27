package com.pubnub.internal.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.eventengine.transition
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation.CancelHandshake
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation.EmitStatus
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation.Handshake
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation.HandshakeReconnect
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation.ReceiveMessages
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.internal.subscribe.eventengine.state.SubscribeState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TransitionFromHandshakingStateTest {
    private val channels = setOf("Channel1")
    private val channelGroups = setOf("ChannelGroup1")
    private val timeToken = 12345345452L
    private val region = "42"
    private val subscriptionCursor = SubscriptionCursor(timeToken, region)
    private val reason = PubNubException("test")

    @Test
    fun `channel and channelGroup should be immutable set`() {
        // given
        val channelName = "Channel01"
        val channelGroupName = "ChannelGroup01"
        val myMutableSetOfChannels = mutableSetOf(channelName)
        val myMutableSetOfChannelGroups = mutableSetOf(channelGroupName)
        val handshaking: SubscribeState.Handshaking =
            SubscribeState.Handshaking(myMutableSetOfChannels, myMutableSetOfChannelGroups, subscriptionCursor = null)

        // when
        myMutableSetOfChannels.remove(channelName)
        myMutableSetOfChannelGroups.remove(channelGroupName)

        // then
        assertTrue(handshaking.channels.contains(channelName))
        assertTrue(handshaking.channelGroups.contains(channelGroupName))
    }

    @Test
    fun can_transit_from_HANDSHAKING_that_has_cursor_that_is_null_to_RECEIVING_when_there_is_HANDSHAKE_SUCCESS_event() {
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Handshaking(channels, channelGroups, subscriptionCursor = null),
                SubscribeEvent.HandshakeSuccess(subscriptionCursor),
            )

        // then
        assertTrue(state is SubscribeState.Receiving)
        val receiving = state as SubscribeState.Receiving
        assertEquals(channels, receiving.channels)
        assertEquals(channelGroups, receiving.channelGroups)
        assertEquals(subscriptionCursor, receiving.subscriptionCursor)
        assertEquals(
            setOf(
                CancelHandshake,
                EmitStatus(
                    PNStatus(
                        PNStatusCategory.PNConnectedCategory,
                        currentTimetoken = timeToken,
                        affectedChannels = channels.toList(),
                        affectedChannelGroups = channelGroups.toList(),
                    ),
                ),
                ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_that_has_cursor_that_is_not_null_to_RECEIVING_when_there_is_HANDSHAKE_SUCCESS_event() {
        // given
        val regionReturnedByHandshake = "12"
        val timeTokenForHandshake = 99945345452L
        val subscriptionCursorForHandshaking = SubscriptionCursor(timeTokenForHandshake, null)
        val subscriptionCursorReturnedByHandshake = SubscriptionCursor(timeToken, regionReturnedByHandshake)

        // when
        val (state, invocations) =
            transition(
                SubscribeState.Handshaking(channels, channelGroups, subscriptionCursor = subscriptionCursorForHandshaking),
                SubscribeEvent.HandshakeSuccess(subscriptionCursorReturnedByHandshake),
            )

        // then
        assertTrue(state is SubscribeState.Receiving)
        val receiving = state as SubscribeState.Receiving
        assertEquals(channels, receiving.channels)
        assertEquals(channelGroups, receiving.channelGroups)
        assertEquals(SubscriptionCursor(timeTokenForHandshake, regionReturnedByHandshake), receiving.subscriptionCursor)
        assertEquals(
            setOf(
                CancelHandshake,
                EmitStatus(
                    PNStatus(
                        PNStatusCategory.PNConnectedCategory,
                        currentTimetoken = timeTokenForHandshake,
                        affectedChannels = channels.toList(),
                        affectedChannelGroups = channelGroups.toList(),
                    ),
                ),
                ReceiveMessages(
                    channels,
                    channelGroups,
                    SubscriptionCursor(timeTokenForHandshake, regionReturnedByHandshake),
                ),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_when_there_is_SUBSCRIPTION_RESTORED_event() {
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Handshaking(channels, channelGroups),
                SubscribeEvent.SubscriptionRestored(channels, channelGroups, subscriptionCursor),
            )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        val handshaking = state as SubscribeState.Handshaking
        assertEquals(channels, handshaking.channels)
        assertEquals(channelGroups, handshaking.channelGroups)
        assertEquals(subscriptionCursor, handshaking.subscriptionCursor)
        assertEquals(
            setOf(
                CancelHandshake,
                Handshake(channels, channelGroups),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_when_there_is_SUBSCRIPTION_CHANGED_event() {
        // given
        val newChannels = setOf("newChannel1")
        val newChannelGroups = setOf("newChannelGroup1")

        // when
        val (state, invocations) =
            transition(
                SubscribeState.Handshaking(channels, channelGroups, subscriptionCursor),
                SubscribeEvent.SubscriptionChanged(newChannels, newChannelGroups),
            )

        // then
        assertTrue(state is SubscribeState.Handshaking)
        val handshaking = state as SubscribeState.Handshaking
        assertEquals(newChannels, handshaking.channels)
        assertEquals(newChannelGroups, handshaking.channelGroups)
        assertEquals(subscriptionCursor, handshaking.subscriptionCursor)
        assertEquals(
            setOf(
                CancelHandshake,
                Handshake(newChannels, newChannelGroups),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_RECONNECTING_when_there_is_HANDSHAKING_FAILURE_event() {
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Handshaking(channels, channelGroups, subscriptionCursor),
                SubscribeEvent.HandshakeFailure(reason),
            )

        // then
        assertTrue(state is SubscribeState.HandshakeReconnecting)
        val handshakeReconnecting = state as SubscribeState.HandshakeReconnecting
        assertEquals(channels, handshakeReconnecting.channels)
        assertEquals(channelGroups, handshakeReconnecting.channelGroups)
        assertEquals(0, handshakeReconnecting.attempts)
        assertEquals(reason, handshakeReconnecting.reason)
        assertEquals(subscriptionCursor, handshakeReconnecting.subscriptionCursor)
        assertEquals(
            setOf(
                CancelHandshake,
                HandshakeReconnect(channels, channelGroups, 0, reason),
            ),
            invocations,
        )
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_HANDSHAKING_STOPPED_when_there_is_DISCONNECT_event() {
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Handshaking(channels, channelGroups),
                SubscribeEvent.Disconnect,
            )

        // then
        assertThat(state, instanceOf(SubscribeState.HandshakeStopped::class.java))
        assertEquals(setOf(CancelHandshake), invocations)
    }

    @Test
    fun can_transit_from_HANDSHAKING_to_UNSUBSRIBED_when_there_is_UNSUBSCRIBE_ALL_event() {
        // when
        val (state, invocations) =
            transition(
                SubscribeState.Handshaking(channels, channelGroups),
                SubscribeEvent.UnsubscribeAll,
            )

        // then
        assertEquals(SubscribeState.Unsubscribed, state)
        assertEquals(setOf(CancelHandshake), invocations)
    }
}
