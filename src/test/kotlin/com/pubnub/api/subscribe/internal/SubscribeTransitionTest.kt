package com.pubnub.api.subscribe.internal

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.models.server.SubscribeMetaData
import com.pubnub.api.state.EffectInvocation
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test

class SubscribeTransitionTest {

    private val channel = "ch1"
    private val tt = 42L
    private val reg = "42"
    private val cursor = Cursor(timetoken = tt, region = reg)
    private val transition = subscribeTransition()

    @Test
    fun happyPath() {
        // given
        val events = listOf(
            SubscriptionChanged(channels = setOf(channel)),
            HandshakingSuccess(cursor = Cursor(timetoken = tt, region = reg)),
            ReceivingSuccess(
                subscribeEnvelope = SubscribeEnvelope(
                    messages = listOf(), metadata = SubscribeMetaData(timetoken = tt, region = reg)
                )
            )
        )

        // when
        val (endState, effects: List<EffectInvocation>) = events.foldIt(transition = transition)
        // then
        assertThat(endState, Matchers.isA(Receiving::class.java))

        val handshake = Handshake(SubscribeExtendedState(channels = setOf(channel)))
        val receiveEvents = ReceiveEvents(
            SubscribeExtendedState(
                channels = setOf(channel), cursor = Cursor(timetoken = tt, region = reg)
            )
        )

        assertThat(
            effects,
            Matchers.contains(
                handshake,
                CancelEffectInvocation(idToCancel = handshake.id()),
                Connected,
                receiveEvents,
                CancelEffectInvocation(idToCancel = receiveEvents.id()),
                EmitEvents(listOf()),
                receiveEvents
            )
        )
    }

    @Test
    fun testReceiveEventsReconnect() {
        // given
        val initialState = Receiving(SubscribeExtendedState(channels = setOf(channel), cursor = cursor))
        val events = listOf(
            ReceivingFailure(
                PNStatus(
                    category = PNStatusCategory.PNTimeoutCategory,
                    error = true,
                    operation = PNOperationType.PNSubscribeOperation
                )
            ),
            ReceiveReconnectingFailure(
                PNStatus(
                    category = PNStatusCategory.PNTimeoutCategory,
                    error = true,
                    operation = PNOperationType.PNSubscribeOperation
                )
            ),
            ReceiveReconnectingFailure(
                PNStatus(
                    category = PNStatusCategory.PNTimeoutCategory,
                    error = true,
                    operation = PNOperationType.PNSubscribeOperation
                )
            ),
            ReceiveReconnectingGiveUp
        )

        // when
        val (endState, effects) = events.foldIt(
            initialState = initialState, transition = transition
        )

        // then
        assertThat(endState, Matchers.isA(ReconnectingFailed::class.java))
        val extendedState = SubscribeExtendedState(
            channels = setOf(channel), cursor = Cursor(timetoken = tt, region = reg)
        )

        val receiveEventsReconnect = ReceiveEventsReconnect(
            subscribeExtendedState = extendedState
        )

        println(effects)
        assertThat(
            effects,
            Matchers.contains(
                CancelEffectInvocation(idToCancel = initialState.call.id()),
                receiveEventsReconnect,
                CancelEffectInvocation(idToCancel = receiveEventsReconnect.id()),
                ReceiveEventsReconnect(
                    subscribeExtendedState = extendedState.copy(attempt = 1)
                ),
                CancelEffectInvocation(idToCancel = receiveEventsReconnect.id()),
                ReceiveEventsReconnect(
                    subscribeExtendedState = extendedState.copy(attempt = 2)
                ),
                CancelEffectInvocation(idToCancel = receiveEventsReconnect.id()),
                ReconnectionFailed
            )
        )
    }

    @Test
    fun testHandshakingReconnection() {
        // given
        val initialState = Handshaking(SubscribeExtendedState(channels = setOf(channel)))
        val events = listOf(
            HandshakingFailure(
                PNStatus(
                    category = PNStatusCategory.PNTimeoutCategory,
                    error = true,
                    operation = PNOperationType.PNSubscribeOperation
                )
            ),
            HandshakingReconnectingFailure(
                PNStatus(
                    category = PNStatusCategory.PNTimeoutCategory,
                    error = true,
                    operation = PNOperationType.PNSubscribeOperation
                )
            ),
            HandshakingReconnectingFailure(
                PNStatus(
                    category = PNStatusCategory.PNTimeoutCategory,
                    error = true,
                    operation = PNOperationType.PNSubscribeOperation
                )
            ),
            HandshakingReconnectingGiveUp
        )

        // when
        val (endState, effects) = events.foldIt(
            initialState = initialState, transition = transition
        )

        // then
        assertThat(endState, Matchers.isA(HandshakingFailed::class.java))
        val extendedState = SubscribeExtendedState(
            channels = setOf(channel)
        )

        val handshakeReconnect = HandshakeReconnect(
            subscribeExtendedState = extendedState
        )

        println(effects)
        assertThat(
            effects,
            Matchers.contains(
                CancelEffectInvocation(idToCancel = initialState.call.id()),
                handshakeReconnect,
                CancelEffectInvocation(idToCancel = handshakeReconnect.id()),
                HandshakeReconnect(
                    subscribeExtendedState = extendedState.copy(attempt = 1)
                ),
                CancelEffectInvocation(idToCancel = handshakeReconnect.id()),
                HandshakeReconnect(
                    subscribeExtendedState = extendedState.copy(attempt = 2)
                ),
                CancelEffectInvocation(idToCancel = handshakeReconnect.id()),
                HandshakeFailed
            )
        )
    }

    private fun List<SubscribeEvent>.foldIt(
        initialState: SubscribeState = Unsubscribed,
        transition: SubscribeTransition
    ): Pair<SubscribeState, List<EffectInvocation>> {
        return fold(initialState to listOf()) { (state, effects), event ->
            val (newState, newEffects) = transition.invoke(state, event)
            newState to effects + newEffects
        }
    }
}
