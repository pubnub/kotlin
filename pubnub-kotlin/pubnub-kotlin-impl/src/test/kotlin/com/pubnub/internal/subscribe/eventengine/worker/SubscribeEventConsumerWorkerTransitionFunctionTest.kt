package com.pubnub.internal.subscribe.eventengine.worker

import com.pubnub.api.PubNubException
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
        val (handshaking, effectInvocationsForSubscriptionChange) =
            transition(
                SubscribeState.Unsubscribed,
                subscriptionChangeSubscribeEvent,
            )
        val (receiving01, effectInvocationsForHandshakingSuccess) =
            transition(
                handshaking,
                SubscribeEvent.HandshakeSuccess(subscriptionCursor),
            )
        val (_, effectInvocationsForReceivingSuccess) =
            transition(
                receiving01,
                SubscribeEvent.ReceiveSuccess(messages, subscriptionCursor),
            )

        // then
        Assertions.assertEquals(
            setOf(
                SubscribeEffectInvocation.Handshake(channels, channelGroups),
                SubscribeEffectInvocation.CancelHandshake,
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        PNStatusCategory.PNConnectedCategory,
                        currentTimetoken = timeToken,
                        affectedChannels = channels.toList(),
                        affectedChannelGroups = channelGroups.toList(),
                    ),
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
                SubscribeEffectInvocation.CancelReceiveMessages,
                SubscribeEffectInvocation.EmitMessages(listOf()),
                SubscribeEffectInvocation.EmitStatus(
                    PNStatus(
                        PNStatusCategory.PNConnectedCategory,
                        currentTimetoken = timeToken,
                        affectedChannels = channels.toList(),
                        affectedChannelGroups = channelGroups.toList(),
                    ),
                ),
                SubscribeEffectInvocation.ReceiveMessages(channels, channelGroups, subscriptionCursor),
            ),
            effectInvocationsForSubscriptionChange + effectInvocationsForHandshakingSuccess + effectInvocationsForReceivingSuccess,
        )
    }

    private val channels = setOf("Channel1")
    private val channelGroups = setOf("ChannelGroup1")
    private val cursor = SubscriptionCursor(12345345452L, "42")
    private val reason = PubNubException("test")

    private fun Set<SubscribeEffectInvocation>.statusCategories() =
        filterIsInstance<SubscribeEffectInvocation.EmitStatus>().map { it.status.category }

    @Test
    fun `RECONNECT in RECEIVING emits no status and a later DISCONNECT still emits PNDisconnectedCategory`() {
        // when
        val (receiving, reconnectInvocations) =
            transition(SubscribeState.Receiving(channels, channelGroups, cursor), SubscribeEvent.Reconnect())
        val (stopped, disconnectInvocations) = transition(receiving, SubscribeEvent.Disconnect)

        // then
        Assertions.assertTrue(reconnectInvocations.statusCategories().isEmpty())
        Assertions.assertTrue(stopped is SubscribeState.ReceiveStopped)
        Assertions.assertEquals(cursor, (stopped as SubscribeState.ReceiveStopped).subscriptionCursor)
        Assertions.assertEquals(listOf(PNStatusCategory.PNDisconnectedCategory), disconnectInvocations.statusCategories())
    }

    @Test
    fun `RECONNECT in RECEIVING emits no status and a later UNSUBSCRIBE_ALL still emits PNDisconnectedCategory`() {
        // when
        val (receiving, reconnectInvocations) =
            transition(SubscribeState.Receiving(channels, channelGroups, cursor), SubscribeEvent.Reconnect())
        val (unsubscribed, unsubscribeInvocations) = transition(receiving, SubscribeEvent.UnsubscribeAll)

        // then
        Assertions.assertTrue(reconnectInvocations.statusCategories().isEmpty())
        Assertions.assertEquals(SubscribeState.Unsubscribed, unsubscribed)
        Assertions.assertEquals(listOf(PNStatusCategory.PNDisconnectedCategory), unsubscribeInvocations.statusCategories())
    }

    /**
     * Known race, not fixed: the receive request cancelled by RECONNECT may still deliver its failure if it was
     * already past the "silenced" check when it was cancelled. Results carry no request id, so the failure is taken
     * as the new request's failure: the state goes to ReceiveFailed, a false PNUnexpectedDisconnectCategory is
     * emitted and the new request is cancelled.
     *
     * Pins today's behavior: the cursor is kept, so a following RECONNECT resumes without a gap. Fixing the race
     * needs per-request ids (follow-up issue); this test should change then.
     */
    @Test
    fun `late RECEIVE_FAILURE after RECONNECT in RECEIVING goes to RECEIVE_FAILED and next RECONNECT keeps cursor`() {
        // when
        val (receiving, _) =
            transition(SubscribeState.Receiving(channels, channelGroups, cursor), SubscribeEvent.Reconnect())
        val (failed, failureInvocations) = transition(receiving, SubscribeEvent.ReceiveFailure(reason))
        val (handshaking, _) = transition(failed, SubscribeEvent.Reconnect())

        // then
        Assertions.assertTrue(failed is SubscribeState.ReceiveFailed)
        Assertions.assertEquals(cursor, (failed as SubscribeState.ReceiveFailed).subscriptionCursor)
        Assertions.assertEquals(
            listOf(PNStatusCategory.PNUnexpectedDisconnectCategory),
            failureInvocations.statusCategories(),
        )
        Assertions.assertTrue(handshaking is SubscribeState.Handshaking)
        Assertions.assertEquals(cursor, (handshaking as SubscribeState.Handshaking).subscriptionCursor)
    }

    /**
     * Known race, not fixed: the receive request cancelled by RECONNECT may still deliver its success if it was
     * already past the "silenced" check when it was cancelled. Results carry no request id, so its messages are
     * emitted and its cursor replaces the current one: the timetoken passed to `reconnect(timetoken)` is silently lost.
     * (With a plain `reconnect()` the new request may return the same messages again, so they can be delivered twice;
     * `dedupOnSubscribe = true` filters them.)
     *
     * Pins today's behavior. Fixing the race needs per-request ids (follow-up issue); this test should change then.
     */
    @Test
    fun `late RECEIVE_SUCCESS after RECONNECT with timetoken in RECEIVING replaces the reconnect cursor`() {
        // given
        val reconnectTimetoken = 99945345452L
        val lateCursor = SubscriptionCursor(22222222222L, "42")

        // when
        val (receiving, _) =
            transition(
                SubscribeState.Receiving(channels, channelGroups, cursor),
                SubscribeEvent.Reconnect(SubscriptionCursor(reconnectTimetoken, null)),
            )
        val (afterLateSuccess, _) = transition(receiving, SubscribeEvent.ReceiveSuccess(listOf(), lateCursor))

        // then
        Assertions.assertTrue(receiving is SubscribeState.Receiving)
        Assertions.assertEquals(
            SubscriptionCursor(reconnectTimetoken, "42"),
            (receiving as SubscribeState.Receiving).subscriptionCursor,
        )
        Assertions.assertTrue(afterLateSuccess is SubscribeState.Receiving)
        Assertions.assertEquals(lateCursor, (afterLateSuccess as SubscribeState.Receiving).subscriptionCursor)
    }

    @Test
    fun `HANDSHAKE_FAILURE after RECONNECT in HANDSHAKING keeps the stored cursor`() {
        // when
        val (handshaking, _) =
            transition(SubscribeState.Handshaking(channels, channelGroups, cursor), SubscribeEvent.Reconnect())
        val (failed, failureInvocations) = transition(handshaking, SubscribeEvent.HandshakeFailure(reason))
        val (nextHandshaking, _) = transition(failed, SubscribeEvent.Reconnect())

        // then
        Assertions.assertTrue(failed is SubscribeState.HandshakeFailed)
        Assertions.assertEquals(cursor, (failed as SubscribeState.HandshakeFailed).subscriptionCursor)
        Assertions.assertEquals(listOf(PNStatusCategory.PNConnectionError), failureInvocations.statusCategories())
        Assertions.assertEquals(cursor, (nextHandshaking as SubscribeState.Handshaking).subscriptionCursor)
    }

    @Test
    fun `HANDSHAKE_SUCCESS after RECONNECT in HANDSHAKING emits PNConnectedCategory once with stored timetoken`() {
        // given
        val handshakeCursor = SubscriptionCursor(33333333333L, "99")

        // when
        val (handshaking, reconnectInvocations) =
            transition(SubscribeState.Handshaking(channels, channelGroups, cursor), SubscribeEvent.Reconnect())
        val (receiving, successInvocations) = transition(handshaking, SubscribeEvent.HandshakeSuccess(handshakeCursor))

        // then
        Assertions.assertTrue(reconnectInvocations.statusCategories().isEmpty())
        Assertions.assertTrue(receiving is SubscribeState.Receiving)
        Assertions.assertEquals(
            SubscriptionCursor(cursor.timetoken, "99"),
            (receiving as SubscribeState.Receiving).subscriptionCursor,
        )
        Assertions.assertEquals(listOf(PNStatusCategory.PNConnectedCategory), successInvocations.statusCategories())
    }

    /**
     * Behavior change: after subscribe(withTimetoken = X) and a failed handshake, RECONNECT without a timetoken
     * resumes from X instead of "now". HandshakeFailed used to drop its stored cursor on RECONNECT, so messages
     * published since X were skipped; now they are delivered first.
     */
    @Test
    fun `RECONNECT after a failed restored handshake resumes from the restored timetoken`() {
        // given
        val restoredCursor = SubscriptionCursor(44444444444L, null)

        // when
        val (handshaking, _) =
            transition(
                SubscribeState.Unsubscribed,
                SubscribeEvent.SubscriptionRestored(channels, channelGroups, restoredCursor),
            )
        val (failed, _) = transition(handshaking, SubscribeEvent.HandshakeFailure(reason))
        val (reconnectHandshaking, _) = transition(failed, SubscribeEvent.Reconnect())

        // then
        Assertions.assertEquals(restoredCursor, (failed as SubscribeState.HandshakeFailed).subscriptionCursor)
        Assertions.assertTrue(reconnectHandshaking is SubscribeState.Handshaking)
        Assertions.assertEquals(restoredCursor, (reconnectHandshaking as SubscribeState.Handshaking).subscriptionCursor)
    }
}
